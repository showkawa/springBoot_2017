package com.kawa.spbgateway.circuitbreaker.resilience4j;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Resilience4jTestHelper {

    /**
     * get the CircuitBreaker status and metrics
     *      * @param prefixName
     * @param circuitBreaker
     */
    public static void getCircuitBreakerStatus(String prefixName, CircuitBreaker circuitBreaker) {

        CircuitBreaker.Metrics metrics = circuitBreaker.getMetrics();
        float failureRate = metrics.getFailureRate();
        int failedCalls = metrics.getNumberOfFailedCalls();
        int successfulCalls = metrics.getNumberOfSuccessfulCalls();
        long notPermittedCalls = metrics.getNumberOfNotPermittedCalls();
        int bufferedCalls = metrics.getNumberOfBufferedCalls();
        float slowCallRate = metrics.getSlowCallRate();
        int slowCalls = metrics.getNumberOfSlowCalls();
        int slowFailedCalls = metrics.getNumberOfSlowFailedCalls();
        int slowSuccessfulCalls = metrics.getNumberOfSlowSuccessfulCalls();

        log.info(prefixName + "state=" + circuitBreaker.getState() + " , metrics[ failureRate=" + failureRate +
                ", bufferedCalls=" + bufferedCalls +
                ", failedCalls=" + failedCalls +
                ", successCalls=" + successfulCalls +
                ", notPermittedCalls=" + notPermittedCalls +
                ", bufferedCalls=" + bufferedCalls +
                ", \n\tslowCallRate=" + slowCallRate +
                ", slowCalls=" + slowCalls +
                ", slowFailedCalls=" + slowFailedCalls +
                ", slowSuccessfulCalls=" + slowSuccessfulCalls +
                " ]"
        );

    }

//    public static void circuitBreakerEventListener(CircuitBreaker circuitBreaker){
//        circuitBreaker.getEventPublisher().onEvent(event -> {
//
//        });
//    }
}
