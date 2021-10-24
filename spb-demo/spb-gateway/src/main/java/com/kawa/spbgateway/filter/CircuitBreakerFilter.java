package com.kawa.spbgateway.filter;

import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreakerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.support.HttpStatusHolder;
import org.springframework.cloud.gateway.support.ServiceUnavailableException;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.reactive.DispatcherHandler;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;
import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.*;

@Slf4j
@Component
public class CircuitBreakerFilter implements GlobalFilter, Ordered {

    @Autowired
    private ReactiveCircuitBreakerFactory reactiveCircuitBreakerFactory;

    private ReactiveCircuitBreaker cb;

    @Autowired
    private DispatcherHandler dispatcherHandlerProvider;

    // do not use this dispatcherHandler directly, use getDispatcherHandler() instead.
    private volatile DispatcherHandler dispatcherHandler;

    private Set<String> defaultStatusCodes = new HashSet<>(Arrays.asList("400", "401", "500"));

//    private URI defaultFallbackUri = URI.create("http://localhost:8080");
    private URI defaultFallbackUri = null;

    public CircuitBreakerFilter() {
    }


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//        URI requestUrl = exchange.getRequiredAttribute(GATEWAY_REQUEST_URL_ATTR);
        URI requestUrl = exchange.getRequest().getURI();
        ReactiveCircuitBreaker cb = reactiveCircuitBreakerFactory.create(requestUrl.getPath());
        Set<HttpStatus> statuses = defaultStatusCodes.stream().map(HttpStatusHolder::parse)
                .filter(statusHolder -> statusHolder.getHttpStatus() != null).map(HttpStatusHolder::getHttpStatus)
                .collect(Collectors.toSet());
        return cb.run(chain.filter(exchange).doOnSuccess(v -> {
            if (statuses.contains(exchange.getResponse().getStatusCode())) {
                HttpStatus status = exchange.getResponse().getStatusCode();
                exchange.getResponse().setStatusCode(null);
                reset(exchange);
                throw new CircuitBreakerStatusCodeException(status);
            }
        }), t -> {
            if (defaultFallbackUri == null) {
                return Mono.error(t);
            }
            URI uri = exchange.getRequest().getURI();
            boolean encoded = containsEncodedParts(uri);
            URI fallbackUrl = UriComponentsBuilder.fromUri(uri).host(null).port(null)
                    .uri(defaultFallbackUri).scheme(null).build(encoded).toUri();
            exchange.getAttributes().put(GATEWAY_REQUEST_URL_ATTR, fallbackUrl);
            addExceptionDetails(t, exchange);

            // Reset the exchange
            reset(exchange);

            ServerHttpRequest request = exchange.getRequest().mutate().uri(requestUrl).build();
            return getDispatcherHandler().handle(exchange.mutate().request(request).build());
        }).onErrorResume(t -> handleErrorWithoutFallback(t));
    }

    @Override
    public int getOrder() {
        return Integer.MIN_VALUE + 100;
    }

    private DispatcherHandler getDispatcherHandler() {
        if (dispatcherHandler == null) {
            dispatcherHandler = dispatcherHandlerProvider;
        }

        return dispatcherHandler;
    }

    private void addExceptionDetails(Throwable t, ServerWebExchange exchange) {
        ofNullable(t).ifPresent(
                exception -> exchange.getAttributes().put(CIRCUITBREAKER_EXECUTION_EXCEPTION_ATTR, exception));
    }

    protected Mono<Void> handleErrorWithoutFallback(Throwable t) {
        if (java.util.concurrent.TimeoutException.class.isInstance(t)) {
            return Mono.error(new ResponseStatusException(HttpStatus.GATEWAY_TIMEOUT, t.getMessage(), t));
        }
        if (CallNotPermittedException.class.isInstance(t)) {
            return Mono.error(new ServiceUnavailableException());
        }
        return Mono.error(t);
    }

    public class CircuitBreakerStatusCodeException extends HttpStatusCodeException {
        public CircuitBreakerStatusCodeException(HttpStatus statusCode) {
            super(statusCode);
        }
    }
}
