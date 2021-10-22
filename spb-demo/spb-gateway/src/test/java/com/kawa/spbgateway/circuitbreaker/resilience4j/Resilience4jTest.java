package com.kawa.spbgateway.circuitbreaker.resilience4j;


import io.github.resilience4j.bulkhead.Bulkhead;
import io.github.resilience4j.bulkhead.BulkheadConfig;
import io.github.resilience4j.bulkhead.BulkheadRegistry;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.vavr.CheckedFunction0;
import io.vavr.CheckedFunction1;
import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.cloud.gateway.support.TimeoutException;

import java.io.IOException;
import java.time.Duration;

@Slf4j
public class Resilience4jTest {

    @Test
    public void circuitBreakerRegistryTest() {
        CircuitBreakerRegistry circuitBreakerRegistry = CircuitBreakerRegistry.of(CircuitBreakerConfig.custom()
                .failureRateThreshold(50)
                .slowCallRateThreshold(50)
                .slowCallDurationThreshold(Duration.ofSeconds(2))
                .waitDurationInOpenState(Duration.ofMillis(1000))
                .permittedNumberOfCallsInHalfOpenState(5)
                .minimumNumberOfCalls(10)
                .slidingWindowType(CircuitBreakerConfig.SlidingWindowType.TIME_BASED)
                .slidingWindowSize(10)
                .recordExceptions(IOException.class, TimeoutException.class)
//                .ignoreExceptions()
                .build());
        CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker(this.getClass().getSimpleName());
        CheckedFunction0<String> decorateCheckedSupplier = CircuitBreaker.decorateCheckedSupplier(circuitBreaker, () -> "kawa");
        Try<String> result = Try.of(decorateCheckedSupplier).map(val -> val + " show");
        log.info(">>>>>>>>>> circuitBreakerRegistryTest {} - {}", result.isSuccess(), result.get());

        // combine  multiple CircuitBreaker
        CircuitBreaker brianT1 = CircuitBreaker.ofDefaults("brianT1");
        CircuitBreaker brianT2 = CircuitBreaker.ofDefaults("brianT2");
        CheckedFunction0<String> decorateCheckedSupplier2 = CircuitBreaker.decorateCheckedSupplier(brianT1, () -> "the first process");
        CheckedFunction1<Object, String> objectStringCheckedFunction = CircuitBreaker.decorateCheckedFunction(brianT2, input -> input + " and the second process");
        Try<String> result2 = Try.of(decorateCheckedSupplier2).mapTry(objectStringCheckedFunction::apply);
        log.info(">>>>>>>>>> circuitBreakerRegistryTest2 {} - {}", result2.isSuccess(), result2.get());

        // recover from exception
        CircuitBreaker brianT13 = CircuitBreaker.ofDefaults("brianT3");
        Try<Object> result3 = Try.of(CircuitBreaker.decorateCheckedSupplier(brianT13, () -> {
            throw new RuntimeException("circuit breaker test");
        })).recover(throwable -> "status OK");
        log.info(">>>>>>>>>> circuitBreakerRegistryTest3 {} - {}", result3.isSuccess(), result3.get());

        // listen the status
        CircuitBreaker.Metrics metrics = brianT13.getMetrics();
        log.info(">>>>>>>>>> circuitBreakerRegistryTest3 {} - {}", metrics.getFailureRate(), metrics.getNumberOfFailedCalls());
    }

    @Test
    public void bulkheadRegistryTest() {
        Bulkhead bulkhead1 = BulkheadRegistry.of(BulkheadConfig.custom()
                .maxConcurrentCalls(10)
                .maxWaitDuration(Duration.ofMillis(500))
                .build()).bulkhead("bulkhead1");
        CheckedFunction0<String> decorateCheckedSupplier = Bulkhead.decorateCheckedSupplier(bulkhead1, () -> "test the ");
        Try<String> result = Try.of(decorateCheckedSupplier).map(val -> val + "Bulkhead");
        log.info(">>>>>>>>>> bulkheadRegistryTest {} - {}", result.isSuccess(), result.get());

    }
}
