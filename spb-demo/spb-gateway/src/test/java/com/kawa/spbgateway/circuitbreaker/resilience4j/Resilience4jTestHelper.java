package com.kawa.spbgateway.circuitbreaker.resilience4j;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.event.CircuitBreakerEvent;
import io.vavr.CheckedConsumer;
import io.vavr.CheckedFunction0;
import lombok.extern.slf4j.Slf4j;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Slf4j
public class Resilience4jTestHelper {

    static int[] container = new int[100];

    /**
     * get the CircuitBreaker status and metrics
     * * @param prefixName
     *
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

        log.info(prefixName + " state=" + circuitBreaker.getState() + " , metrics[ failureRate=" + failureRate +
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

    public static void circuitBreakerEventListener(CircuitBreaker circuitBreaker) {
        circuitBreaker.getEventPublisher()
                .onSuccess(event -> {
                    log.info("---------- call service success:{}", event.toString());
                })
                .onError(event -> {
                    log.info("---------- call service failed:{}", event.toString());
                    Throwable throwable = event.getThrowable();
                    if(throwable instanceof TimeoutException){
                        // TODO record to slow call
                    }
                })
                .onIgnoredError(event -> {
                    log.info("---------- call service failed and ignore exception:{}", event.toString());
                })
                .onReset(event -> {
                    log.info("---------- circuit breaker reset:{}", event.toString());
                })
                .onStateTransition(event -> {
                    log.info("---------- circuit breaker status updated:{}", event.toString());
                })
                .onCallNotPermitted(event -> {
                    log.info("---------- circuit breaker opened:{}", event.toString());
                })
                .onFailureRateExceeded(event -> {
                    log.info("---------- exceeded failure rate:{}", event.toString());
                })
                .onSlowCallRateExceeded(event -> {
                    log.info("---------- exceeded slow call rate:{}", event.toString());
                });
    }

    public static void recordResponseToCircuitBreaker(CircuitBreaker circuitBreaker, WebTestClient testClient, String path) {
        WebTestClient.ResponseSpec responseSpec = testClient.post().uri(path).exchange();
        try {
            responseSpec.expectStatus().is2xxSuccessful();
            circuitBreaker.onSuccess(0, TimeUnit.MILLISECONDS);
            return;
        } catch (Throwable error) {
        }

        try {
            responseSpec.expectStatus().is4xxClientError();
            circuitBreaker.onError(0, TimeUnit.MILLISECONDS, new Throwable("<<<< hit 4XX >>>>"));
            return;
        } catch (Throwable error) {
        }

        try {
            responseSpec.expectStatus().is5xxServerError();
            circuitBreaker.onError(0, TimeUnit.MILLISECONDS, new Throwable("<<<< hit 5XX >>>>"));
        } catch (Throwable error) {
            circuitBreaker.onError(0, TimeUnit.MILLISECONDS, new Throwable("<<<< hit unknown error >>>>"));
            return;
        }
    }

    public static String responseToCircuitBreaker(CircuitBreaker circuitBreaker, WebTestClient testClient, String path) {
        WebTestClient.ResponseSpec responseSpec = testClient.post().uri(path).exchange();
        try {
            responseSpec.expectStatus().is4xxClientError();
            circuitBreaker.onError(0, TimeUnit.MILLISECONDS, new Throwable("<<<<< hit 4XX >>>>>"));
            new RuntimeException("<<<<< hit 4XX >>>>>");
        } catch (Throwable error) {
        }

        try {
            responseSpec.expectStatus().is5xxServerError();
            circuitBreaker.onError(0, TimeUnit.MILLISECONDS, new Throwable("<<<<< hit 5XX >>>>>"));
            new RuntimeException("<<<<< hit 5XX >>>>>");
        } catch (Throwable error) {
        }
        responseSpec.expectStatus().is2xxSuccessful();
        circuitBreaker.onSuccess(0, TimeUnit.MILLISECONDS);
        return "hit 200";
    }

    public static boolean weightBoolean() {
        if (container[0] != 1) {
            for (int i = 0; i < 80; i++) {
                container[i] = 1;
            }
            for (int i = 80; i < 100; i++) {
                container[i] = 0;
            }
        }
        int index = (int) (Math.random() * 100);
        return container[index] == 1;
    }
}
