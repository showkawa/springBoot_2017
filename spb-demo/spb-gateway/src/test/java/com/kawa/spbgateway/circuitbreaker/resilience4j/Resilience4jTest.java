package com.kawa.spbgateway.circuitbreaker.resilience4j;


import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.vavr.CheckedFunction0;
import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.time.Duration;

@Slf4j
public class Resilience4jTest {

    @Test
    public void circuitBreakerRegistryTest(){
        CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig.custom()
                .failureRateThreshold(50)
                .waitDurationInOpenState(Duration.ofMillis(1000))
                .ringBufferSizeInHalfOpenState(2)
                .ringBufferSizeInClosedState(2)
                .build();
        CircuitBreakerRegistry circuitBreakerRegistry = CircuitBreakerRegistry.of(circuitBreakerConfig);
        CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker(this.getClass().getSimpleName());
        CheckedFunction0<String> decorateCheckedSupplier = CircuitBreaker.decorateCheckedSupplier(circuitBreaker, () -> "kawa");
        Try<String> result = Try.of(decorateCheckedSupplier).map(val -> val + " show");
        log.info(">>>>>>>>>> circuitBreakerRegistryTest {} - {}",result.isSuccess(),result.get());


    }
}
