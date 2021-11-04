package com.kawa.spbgateway.circuitbreaker.resilience4j;

import io.github.resilience4j.bulkhead.Bulkhead;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.retry.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Slf4j
public class Resilience4jTestHelper {

    static int[] container = new int[100];

    static int[] fallbackContainer = new int[100];

    /**
     * get the CircuitBreaker status and metrics
     *
     * @param prefixName
     * @param circuitBreaker
     * @return circuitBreaker state
     */
    public static String getCircuitBreakerStatus(String prefixName, CircuitBreaker circuitBreaker) {

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
        log.info(prefixName + " circuitBreaker tags:{}", circuitBreaker.getTags());
        return circuitBreaker.getState().name();
    }

    public static void circuitBreakerEventListener(CircuitBreaker circuitBreaker) {
        circuitBreaker.getEventPublisher()
                .onSuccess(event -> log.info("---------- CircuitBreakerEvent:{}  CircuitBreakerName:{}", event.getEventType(), event.getCircuitBreakerName()))
                .onError(event -> {
                    log.info("---------- CircuitBreakerEvent:{}  CircuitBreakerName:{}", event.getEventType(), event.getCircuitBreakerName());
                    Throwable throwable = event.getThrowable();
                    if (throwable instanceof TimeoutException) {
                        // TODO record to slow call
                    }
                })
                .onIgnoredError(event -> log.info("---------- CircuitBreakerEvent:{}  CircuitBreakerName:{}", event.getEventType(), event.getCircuitBreakerName()))
                .onReset(event -> log.info("---------- CircuitBreakerEvent:{}  CircuitBreakerName:{}", event.getEventType(), event.getCircuitBreakerName()))
                .onStateTransition(event -> log.info("---------- CircuitBreakerEvent:{}  CircuitBreakerName:{}", event.getEventType(), event.getCircuitBreakerName()))
                .onCallNotPermitted(event -> log.info("---------- CircuitBreakerEvent:{}  CircuitBreakerName:{}", event.getEventType(), event.getCircuitBreakerName()))
                .onFailureRateExceeded(event -> log.info("---------- CircuitBreakerEvent:{}  CircuitBreakerName:{}", event.getEventType(), event.getCircuitBreakerName()))
                .onSlowCallRateExceeded(event -> log.info("---------- CircuitBreakerEvent:{}  CircuitBreakerName:{}", event.getEventType(), event.getCircuitBreakerName()));
    }

    /**
     * get the Retry status and metrics
     * * @param prefixName
     *
     * @param retry
     */
    public static void getRetryStatus(String prefixName, Retry retry) {

        Retry.Metrics metrics = retry.getMetrics();
        long successfulCallsWithRetryAttempt = metrics.getNumberOfSuccessfulCallsWithRetryAttempt();
        long successfulCallsWithoutRetryAttempt = metrics.getNumberOfSuccessfulCallsWithoutRetryAttempt();
        long failedCallsWithRetryAttempt = metrics.getNumberOfFailedCallsWithRetryAttempt();
        long failedCallsWithoutRetryAttempt = metrics.getNumberOfFailedCallsWithoutRetryAttempt();

        log.info(prefixName + " -> retry metrics[ successfulCallsWithRetryAttempt=" + successfulCallsWithRetryAttempt +
                ", successfulCallsWithoutRetryAttempt=" + successfulCallsWithoutRetryAttempt +
                ", failedCallsWithRetryAttempt=" + failedCallsWithRetryAttempt +
                ", failedCallsWithoutRetryAttempt=" + failedCallsWithoutRetryAttempt +
                " ]"
        );
    }

    public static void retryEventListener(Retry retry) {
        retry.getEventPublisher()
                .onSuccess(event -> log.info("))))))))))) retry service success:{}", event))
                .onError(event -> {
                    log.info("))))))))))) retry service failed:{}", event);
                    Throwable exception = event.getLastThrowable();
                    if (exception instanceof TimeoutException) {
                        // TODO
                    }
                })
                .onIgnoredError(event -> log.info("))))))))))) retry service failed and ignore:{}", event))
                .onRetry(event -> log.info("))))))))))) retry call service: {}", event.getNumberOfRetryAttempts()));

    }

    /**
     * get the Bulkhead status and metrics
     * * @param prefixName
     *
     * @param bulkhead
     */
    public static void getBulkheadStatus(String prefixName, Bulkhead bulkhead) {
        Bulkhead.Metrics metrics = bulkhead.getMetrics();
        int availableCalls = metrics.getAvailableConcurrentCalls();
        int maxCalls = metrics.getMaxAllowedConcurrentCalls();
        log.info(prefixName + "bulkhead metrics[ availableCalls=" + availableCalls +
                ", maxCalls=" + maxCalls + " ]"
        );
    }

    public static void bulkheadEventListener(Bulkhead bulkhead) {
        bulkhead.getEventPublisher()
                .onCallPermitted(event -> log.info("---------- call service permitted:{}", event))
                .onCallRejected(event -> log.info("---------- call service rejected:{}", event));

    }

    /**
     * get the RateLimiter status and metrics
     * * @param prefixName
     *
     * @param rateLimiter
     */
    public static void getRateLimiterStatus(String prefixName, RateLimiter rateLimiter) {
        RateLimiter.Metrics metrics = rateLimiter.getMetrics();
        int availablePermissions = metrics.getAvailablePermissions();
        int waitingThreads = metrics.getNumberOfWaitingThreads();
        log.info(prefixName + "rateLimiter metrics[ availablePermissions=" + availablePermissions +
                ", waitingThreads=" + waitingThreads + " ]"
        );
    }

    public static void rateLimiterEventListener(RateLimiter rateLimiter) {
        rateLimiter.getEventPublisher()
                .onSuccess(event -> log.info("---------- rateLimiter success:{}", event))
                .onFailure(event -> log.info("---------- rateLimiter failure:{}", event));
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
            circuitBreaker.onError(0, TimeUnit.MILLISECONDS, new RuntimeException("<<<< hit 4XX >>>>"));
            return;
        } catch (Throwable error) {
        }

        try {
            responseSpec.expectStatus().is5xxServerError();
            circuitBreaker.onError(0, TimeUnit.MILLISECONDS, new RuntimeException("<<<< hit 5XX >>>>"));
        } catch (Throwable error) {
            circuitBreaker.onError(0, TimeUnit.MILLISECONDS, new RuntimeException("<<<< hit unknown error >>>>"));
            return;
        }
    }

    public static void recordSlowCallResponseToCircuitBreaker(CircuitBreaker circuitBreaker, WebTestClient testClient, String path) {
        WebTestClient.ResponseSpec responseSpec = testClient.post().uri(path).exchange();
        try {
            responseSpec.expectStatus().is2xxSuccessful();
            return;
        } catch (Throwable error) {
        }

        try {
            responseSpec.expectStatus().is4xxClientError();
            circuitBreaker.onError(0, TimeUnit.MILLISECONDS, new RuntimeException("<<<< hit 4XX >>>>"));
            return;
        } catch (Throwable error) {
        }

        try {
            responseSpec.expectStatus().is5xxServerError();
            circuitBreaker.onError(0, TimeUnit.MILLISECONDS, new RuntimeException("<<<< hit 5XX >>>>"));
        } catch (Throwable error) {
            circuitBreaker.onError(0, TimeUnit.MILLISECONDS, new RuntimeException("<<<< hit unknown error >>>>"));
            return;
        }
    }

    public static String response(WebTestClient testClient, String path) {
        WebTestClient.ResponseSpec responseSpec = testClient.post().uri(path).exchange();
        try {
            responseSpec.expectStatus().is4xxClientError();
            throw new RuntimeException("<<<<< hit 4XX >>>>>");
        } catch (Throwable error) {
        }

        try {
            responseSpec.expectStatus().is5xxServerError();
            throw new RuntimeException("<<<<< hit 5XX >>>>>");
        } catch (Throwable error) {
        }
        responseSpec.expectStatus().is2xxSuccessful();
        return "hit 200";
    }

    public static String responseToCircuitBreaker(CircuitBreaker circuitBreaker, WebTestClient testClient, String path) {
        WebTestClient.ResponseSpec responseSpec = testClient.post().uri(path).exchange();
        try {
            responseSpec.expectStatus().is4xxClientError();
            circuitBreaker.onError(0, TimeUnit.MILLISECONDS, new RuntimeException("<<<<< hit 4XX >>>>>"));
            throw new RuntimeException("<<<<< hit 4XX >>>>>");
        } catch (Throwable error) {
        }

        try {
            responseSpec.expectStatus().is5xxServerError();
            circuitBreaker.onError(0, TimeUnit.MILLISECONDS, new RuntimeException("<<<<< hit 5XX >>>>>"));
            throw new RuntimeException("<<<<< hit 5XX >>>>>");
        } catch (Throwable error) {
        }
        responseSpec.expectStatus().is2xxSuccessful();
        return "hit 200";
    }

    public static String responseToRetry(CircuitBreaker circuitBreaker, Retry retry, WebTestClient testClient, String path) {
        WebTestClient.ResponseSpec responseSpec = testClient.post().uri(path).exchange();
        try {
            responseSpec.expectStatus().is4xxClientError();
            circuitBreaker.onError(0, TimeUnit.MILLISECONDS, new RuntimeException("<<<<< hit 4XX >>>>>"));
            retry.context().onError(new RuntimeException("<<<<< hit 4XX >>>>>"));
            throw new RuntimeException("<<<<< hit 4XX >>>>>");
        } catch (Throwable error) {
        }

        try {
            responseSpec.expectStatus().is5xxServerError();
            circuitBreaker.onError(0, TimeUnit.MILLISECONDS, new RuntimeException("<<<<< hit 5XX >>>>>"));
            retry.context().onRuntimeError(new RuntimeException("<<<<< hit 5XX >>>>>"));
            throw new RuntimeException("<<<<< hit 5XX >>>>>");
        } catch (Throwable error) {
        }
        responseSpec.expectStatus().is2xxSuccessful();
        circuitBreaker.onSuccess(0, TimeUnit.MILLISECONDS);
        retry.context().onComplete();
        return "hit 200";
    }

    public static boolean expectError() {
        if (fallbackContainer[0] != 1) {
            for (int i = 0; i < 90; i++) {
                fallbackContainer[i] = 1;
            }
            for (int i = 90; i < 100; i++) {
                fallbackContainer[i] = 0;
            }
        }
        int index = (int) (Math.random() * 100);
        return fallbackContainer[index] == 1;
    }

    public static boolean releasePermission() {
        if (container[0] != 1) {
            for (int i = 0; i < 70; i++) {
                container[i] = 1;
            }
            for (int i = 70; i < 100; i++) {
                container[i] = 0;
            }
        }
        int index = (int) (Math.random() * 100);
        return container[index] == 1;
    }
}
