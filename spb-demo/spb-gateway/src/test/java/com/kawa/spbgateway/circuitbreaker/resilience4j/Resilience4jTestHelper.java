package com.kawa.spbgateway.circuitbreaker.resilience4j;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Slf4j
public class Resilience4jTestHelper {

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
                    log.info(">>>>>>>>>> call service success:{}",event.toString());
                })
                .onError(event -> {
                    log.info(">>>>>>>>>> call service failed:{}",event.toString());
                })
                .onIgnoredError(event -> {
                    log.info(">>>>>>>>>> call service failed and ignore exception:{}",event.toString());
                })
                .onReset(event -> {
                    log.info(">>>>>>>>>> circuit breaker reset:{}",event.toString());
                })
                .onStateTransition(event -> {
                    log.info(">>>>>>>>>> circuit breaker status updated:{}",event.toString());
                })
                .onCallNotPermitted(event -> {
                    log.info(">>>>>>>>>> circuit breaker opened:{}",event.toString());
                })
                .onFailureRateExceeded(event -> {
                    log.info(">>>>>>>>>> exceeded failure rate:{}",event.toString());
                })
                .onSlowCallRateExceeded(event -> {
                    log.info(">>>>>>>>>> exceeded slow call rate:{}",event.toString());
                });
    }

    public static void recordResponseToCircuitBreaker(CircuitBreaker circuitBreaker, WebTestClient testClient, String path) {
        WebTestClient.ResponseSpec responseSpec = testClient.post().uri(path).exchange();
        try{
//            responseSpec.expectStatus().is1xxInformational();
            responseSpec.expectStatus().is2xxSuccessful();
//            responseSpec.expectStatus().is3xxRedirection();
            circuitBreaker.onSuccess(0, TimeUnit.MILLISECONDS);
            return;
        }catch (Throwable error){ }

        try{
            responseSpec.expectStatus().is4xxClientError();
            circuitBreaker.onError(0,TimeUnit.MILLISECONDS,new Throwable("<<<< hit 4XX >>>>"));
            return;
        }catch (Throwable error){}

        try{
            responseSpec.expectStatus().is5xxServerError();
            circuitBreaker.onError(0,TimeUnit.MILLISECONDS,new Throwable("<<<< hit 5XX >>>>"));
        }catch (Throwable error){
            circuitBreaker.onError(0,TimeUnit.MILLISECONDS,new Throwable("<<<< hit unknown error >>>>"));
            return;
        }

    }
}
