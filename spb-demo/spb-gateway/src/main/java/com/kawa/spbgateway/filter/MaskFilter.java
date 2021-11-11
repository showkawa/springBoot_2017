package com.kawa.spbgateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.NettyWriteResponseFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Optional;


@Component
@Slf4j
public class MaskFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        putHeaderToLog(exchange, "x-request-region");
        putHeaderToLog(exchange, "x-request-id");
        ServerHttpRequest request = exchange.getRequest();
        log.info("=== request ===:{}", request.getURI());
        return chain.filter(exchange.mutate().request(request).build());
    }

    @Override
    public int getOrder() {
        return NettyWriteResponseFilter.WRITE_RESPONSE_FILTER_ORDER - 1;
    }


    private void putHeaderToLog(ServerWebExchange exchange, String key) {
        MDC.put(key, "");
        ServerHttpRequest request = exchange.getRequest();
        HttpHeaders headers = request.getHeaders();
        if (null == headers.get(key) || headers.get(key).isEmpty()) {
            return;
        }
        Optional<String> result = headers.get(key).stream().findFirst();
        result.ifPresent(val -> MDC.put(key, val));
    }
}
