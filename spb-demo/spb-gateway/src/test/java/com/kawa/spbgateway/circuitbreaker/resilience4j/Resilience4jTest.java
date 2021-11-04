package com.kawa.spbgateway.circuitbreaker.resilience4j;


import com.github.tomakehurst.wiremock.junit.WireMockRule;
import io.github.resilience4j.bulkhead.Bulkhead;
import io.github.resilience4j.bulkhead.BulkheadConfig;
import io.github.resilience4j.bulkhead.BulkheadRegistry;
import io.github.resilience4j.bulkhead.internal.InMemoryBulkheadRegistry;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.circuitbreaker.internal.InMemoryCircuitBreakerRegistry;
import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import io.github.resilience4j.ratelimiter.internal.InMemoryRateLimiterRegistry;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import io.github.resilience4j.retry.RetryRegistry;
import io.github.resilience4j.retry.internal.InMemoryRetryRegistry;
import io.github.resilience4j.timelimiter.TimeLimiter;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import io.github.resilience4j.timelimiter.TimeLimiterRegistry;
import io.github.resilience4j.timelimiter.internal.InMemoryTimeLimiterRegistry;
import io.vavr.CheckedFunction0;
import io.vavr.CheckedFunction1;
import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.springframework.cloud.gateway.support.TimeoutException;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.netty.http.client.HttpClient;

import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static com.kawa.spbgateway.circuitbreaker.resilience4j.Resilience4jTestHelper.releasePermission;
import static com.kawa.spbgateway.circuitbreaker.resilience4j.Resilience4jTestHelper.weightBoolean;

@Slf4j
@TestPropertySource(properties = {
        "logging.config=./src/test/resources/logback.xml"
})
public class Resilience4jTest {

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(wireMockConfig().port(8080));

    private WebTestClient testClient;

    private CircuitBreakerRegistry circuitBreakerRegistry;

    private TimeLimiterRegistry timeLimiterRegistry;

    private BulkheadRegistry bulkheadRegistry;

    private RetryRegistry retryRegistry;

    private RateLimiterRegistry rateLimiterRegistry;

    private RateLimiter rateLimiter;

    private Bulkhead bulkhead;

    private Retry retry;

    private TimeLimiter timeLimiter;

    private CircuitBreaker circuitBreaker;

    private String PATH_200 = "/api/pancake/v1/yee/query";

    private String PATH_400 = "/api/hk/card/v1/er/query";

    private String PATH_408 = "/api/pancake/v1/coin/query";

    private String PATH_500 = "/api/hk/card/v1/card/query";


    @Before
    public void setup() {
        HttpClient httpClient = HttpClient.create().wiretap(true);
        testClient = WebTestClient.bindToServer(new ReactorClientHttpConnector(httpClient))
                .baseUrl("http://localhost:8080")
                .responseTimeout(Duration.ofDays(1))
                .build();

        circuitBreakerRegistry = new InMemoryCircuitBreakerRegistry();
        circuitBreaker = circuitBreakerRegistry.circuitBreaker("resilience4jTest",
                CircuitBreakerConfig
                        .custom()
                        .failureRateThreshold(50)
                        .slowCallRateThreshold(90)
                        .slowCallDurationThreshold(Duration.ofMillis(1000*1))
                        .minimumNumberOfCalls(10)
                        .slidingWindowType(CircuitBreakerConfig.SlidingWindowType.COUNT_BASED)
                        .slidingWindowSize(10)
                        .build());

        timeLimiterRegistry = new InMemoryTimeLimiterRegistry();
        timeLimiter = timeLimiterRegistry.timeLimiter("resilience4jTest",
                TimeLimiterConfig
                        .custom()
                        .timeoutDuration(Duration.ofMillis(1000 * 1))
                        .cancelRunningFuture(true)
                        .build());

        retryRegistry = new InMemoryRetryRegistry();
        retry = retryRegistry.retry("resilience4jTest",
                RetryConfig
                        .custom()
                        .maxAttempts(3)
                        .waitDuration(Duration.ofMillis(1000 * 1))
                        .retryExceptions(RuntimeException.class)
                        .build());

        bulkheadRegistry = new InMemoryBulkheadRegistry();
        bulkhead = bulkheadRegistry.bulkhead("resilience4jTest",
                BulkheadConfig
                        .custom()
                        .maxConcurrentCalls(10)
                        .maxWaitDuration(Duration.ofSeconds(1))
                        .build());

        rateLimiterRegistry = new InMemoryRateLimiterRegistry();
        rateLimiter = rateLimiterRegistry.rateLimiter("resilience4jTest",
                RateLimiterConfig
                        .custom()
                        .timeoutDuration(Duration.ofMillis(500))
                        .limitRefreshPeriod(Duration.ofSeconds(1))
                        .limitForPeriod(20)
                        .build());
        Resilience4jTestHelper.circuitBreakerEventListener(circuitBreaker);
        Resilience4jTestHelper.retryEventListener(retry);
        Resilience4jTestHelper.bulkheadEventListener(bulkhead);
        Resilience4jTestHelper.rateLimiterEventListener(rateLimiter);

        stubFor(post(urlMatching(PATH_200))
                .willReturn(okJson("{}")));

        stubFor(post(urlMatching(PATH_400))
                .willReturn(badRequest()));

        stubFor(post(urlMatching(PATH_408))
                .willReturn(okJson("{\"message\":\"time out\"}").withFixedDelay(1000 * 2)));

        stubFor(post(urlMatching(PATH_500))
                .willReturn(serverError()));
    }


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

    /**
     * use executeCheckedSupplier or executeSupplier no need use degradation
     */
    @Test
    public void When_CircuitBreaker_Expect_Open() {
        AtomicInteger count = new AtomicInteger();
        try {
            for (int i = 0; i < 10; i++) {
                circuitBreaker.executeCheckedSupplier(() -> {
                    Resilience4jTestHelper.recordResponseToCircuitBreaker(circuitBreaker, testClient, PATH_200);
                    Resilience4jTestHelper.getCircuitBreakerStatus(">>>>>>>>>> end call " + count.incrementAndGet(), circuitBreaker);
                    return null;
                });
            }
            for (int i = 0; i < 10; i++) {
                circuitBreaker.executeCheckedSupplier(() -> {
                    Resilience4jTestHelper.recordResponseToCircuitBreaker(circuitBreaker, testClient, PATH_408);
                    Resilience4jTestHelper.getCircuitBreakerStatus(">>>>>>>>>> end call " + count.incrementAndGet(), circuitBreaker);
                    return null;
                });
            }
            for (int i = 0; i < 10; i++) {
                circuitBreaker.executeCheckedSupplier(() -> {
                    Resilience4jTestHelper.recordResponseToCircuitBreaker(circuitBreaker, testClient, PATH_400);
                    Resilience4jTestHelper.getCircuitBreakerStatus(">>>>>>>>>> end call " + count.incrementAndGet(), circuitBreaker);
                    return null;
                });
            }

        } catch (Throwable error) {
            log.error(String.format(">>>>>>>>>> %s", error.getMessage()));
        }
    }

    @Test
    public void When_CircuitBreaker_Expect_Fallback() {
        AtomicInteger count = new AtomicInteger();
        for (int i = 0; i < 100; i++) {
            String path = weightBoolean() ? PATH_500 : PATH_200;
            CheckedFunction0<String> response =
                    circuitBreaker.decorateCheckedSupplier(() -> Resilience4jTestHelper.responseToCircuitBreaker(circuitBreaker, testClient, path));
            Try<String> result = Try.of(response).recover(CallNotPermittedException.class, throwable -> {
                Resilience4jTestHelper.getCircuitBreakerStatus(">>>>>>>>>> open CircuitBreaker " + count.incrementAndGet(), circuitBreaker);
                return "hit CircuitBreaker";
            }).recover(throwable -> {
                Resilience4jTestHelper.getCircuitBreakerStatus(">>>>>>>>>> call fallback " + count.incrementAndGet(), circuitBreaker);
                return "hit fallback";
            });
            Resilience4jTestHelper.getCircuitBreakerStatus(">>>>>>>>>> call end " + count.incrementAndGet(), circuitBreaker);
            log.info(">>>>>>>>>> result:{}", result.get());
        }

    }

    @Test
    public void When_CircuitBreaker_Expect_Timeout() {
        AtomicInteger count = new AtomicInteger();
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 30; i++) {
            String path = weightBoolean() ? PATH_408 : PATH_200;
            Future<String> futureStr =
                    executorService.submit(() -> Resilience4jTestHelper.responseToCircuitBreaker(circuitBreaker, testClient, path));
            Callable<String> stringCallable = timeLimiter.decorateFutureSupplier(() -> futureStr);
            Callable<String> response = circuitBreaker.decorateCallable(stringCallable);
            Try.of(response::call).recover(CallNotPermittedException.class, throwable -> {
                Resilience4jTestHelper.getCircuitBreakerStatus(">>>>>>>>>> open CircuitBreaker " + count.incrementAndGet(), circuitBreaker);
                return "hit CircuitBreaker";
            }).recover(throwable -> {
                Resilience4jTestHelper.getCircuitBreakerStatus(">>>>>>>>>> call fallback " + count.incrementAndGet(), circuitBreaker);
                return "hit fallback";
            });
        }
    }

    @Test
    public void When_CircuitBreaker_Expect_Retry() {
        AtomicInteger count = new AtomicInteger();
        for (int i = 0; i < 30; i++) {
            String path = weightBoolean() ? PATH_200 : PATH_500;
            Callable<String> stringCallable = Retry.decorateCallable(retry, () -> Resilience4jTestHelper.responseToRetry(circuitBreaker, retry, testClient, path));
            Callable<String> response = circuitBreaker.decorateCallable(stringCallable);
            Try.of(response::call).andThen(val -> {
                log.info(">>>>>>>>>> result {}: {}", count.incrementAndGet(), val);
            }).recover(CallNotPermittedException.class, throwable -> {
                Resilience4jTestHelper.getCircuitBreakerStatus(">>>>>>>>>> open circuitBreaker " + count.incrementAndGet(), circuitBreaker);
                Resilience4jTestHelper.getRetryStatus("))))))))))", retry);
                return "hit CircuitBreaker";
            }).recover(throwable -> {
                Resilience4jTestHelper.getCircuitBreakerStatus(">>>>>>>>>> call fallback " + count.incrementAndGet(), circuitBreaker);
                Resilience4jTestHelper.getRetryStatus("))))))))))", retry);
                return "hit fallback";
            });
        }
    }

    @Test
    public void When_CircuitBreaker_Expect_Hit_Bulkhead_MaxCall() throws Exception {
        AtomicInteger count = new AtomicInteger();
        ExecutorService executorService = Executors.newFixedThreadPool(50);
        for (int i = 0; i < 100; i++) {
            if (bulkhead.tryAcquirePermission()) {
                bulkhead.acquirePermission();
            }
            Future<String> futureStr =
                    executorService.submit(() -> Resilience4jTestHelper.response(testClient, PATH_200));
            Future<String> stringFuture = bulkhead.executeCallable(() -> futureStr);
            Try.of(stringFuture::get).andThen(val -> {
                log.info(">>>>>>>>>> result {}: {}", count.incrementAndGet(), val);
            }).recover(throwable -> {
                log.info(">>>>>>>>>> exception {}: {}", count.incrementAndGet(), throwable.getMessage());
                return "hit fallback";
            });
            if (releasePermission()) {
                bulkhead.releasePermission();
                bulkhead.onComplete();
            }
            Resilience4jTestHelper.getBulkheadStatus(")))))))))) ", bulkhead);
        }
        executorService.shutdown();
    }

    @Test
    public void When_CircuitBreaker_Expect_Hit_RateLimiter() throws Exception {
        AtomicInteger count = new AtomicInteger();
        ExecutorService executorService = Executors.newFixedThreadPool(50);
        for (int i = 0; i < 100; i++) {
            Future<String> futureStr =
                    executorService.submit(() -> Resilience4jTestHelper.response(testClient, PATH_200));
            Future<String> stringFuture = rateLimiter.executeCallable(() -> futureStr);
            Try.of(stringFuture::get).andThen(val -> {
                log.info(">>>>>>>>>> result {}: {}", count.incrementAndGet(), val);
            }).recover(throwable -> {
                log.info(">>>>>>>>>> exception {}: {}", count.incrementAndGet(), throwable.getMessage());
                return "hit fallback";
            });
            Resilience4jTestHelper.getRateLimiterStatus(")))))))))) ", rateLimiter);
        }
        executorService.shutdown();
    }


}
