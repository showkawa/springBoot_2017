package com.kawa.spbgateway.circuitbreaker.resilience4j;


import com.github.tomakehurst.wiremock.junit.WireMockRule;
import io.github.resilience4j.bulkhead.Bulkhead;
import io.github.resilience4j.bulkhead.BulkheadConfig;
import io.github.resilience4j.bulkhead.BulkheadFullException;
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
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
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
import io.vavr.collection.HashMap;
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
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static com.kawa.spbgateway.circuitbreaker.resilience4j.Resilience4jTestHelper.expectError;
import static com.kawa.spbgateway.circuitbreaker.resilience4j.Resilience4jTestHelper.releasePermission;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@TestPropertySource(properties = {
        "logging.config=./src/test/resources/logback.xml"
})
public class Resilience4jTest {

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(wireMockConfig().port(8080));

    private WebTestClient testClient;

    private CircuitBreakerRegistry circuitBreakerRegistry;

    private BulkheadRegistry bulkheadRegistry;

    private TimeLimiterRegistry timeLimiterRegistry;

    private RetryRegistry retryRegistry;

    private RateLimiterRegistry rateLimiterRegistry;

    private CircuitBreaker circuitBreaker;

    private CircuitBreaker circuitBreakerWithTags;

    private CircuitBreakerConfig circuitBreakerConfig;

    private RateLimiter rateLimiter;

    private Retry retry;

    private TimeLimiter timeLimiter;


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
        circuitBreakerConfig = CircuitBreakerConfig
                .custom()
                .failureRateThreshold(70)
                .slowCallRateThreshold(90)
                .slowCallDurationThreshold(Duration.ofMillis(1000 * 1))
                .minimumNumberOfCalls(10)
                .slidingWindowType(CircuitBreakerConfig.SlidingWindowType.COUNT_BASED)
                .slidingWindowSize(10)
                .build();
        circuitBreaker = circuitBreakerRegistry.circuitBreaker("resilience4jTest", circuitBreakerConfig);

        bulkheadRegistry = new InMemoryBulkheadRegistry();


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
                        .maxAttempts(5)
                        .waitDuration(Duration.ofMillis(500))
                        .retryOnResult(val -> val.toString().contains("HIT_ERROR_"))
//                        .retryExceptions(RuntimeException.class)
                        .build());

        rateLimiterRegistry = new InMemoryRateLimiterRegistry();
        rateLimiter = rateLimiterRegistry.rateLimiter("resilience4jTest",
                RateLimiterConfig
                        .custom()
                        .timeoutDuration(Duration.ofMillis(100))
                        .limitRefreshPeriod(Duration.ofSeconds(1))
                        .limitForPeriod(20)
                        .build());
        Resilience4jTestHelper.circuitBreakerEventListener(circuitBreaker);
        Resilience4jTestHelper.retryEventListener(retry);
        Resilience4jTestHelper.rateLimiterEventListener(rateLimiter);
        Resilience4jTestHelper.timeLimiterEventListener(timeLimiter);

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
    public void When_Test_CircuitBreaker_Expect_Close() {
        AtomicInteger count = new AtomicInteger();
        for (int i = 0; i < 10; i++) {
            Resilience4jTestHelper.recordResponseToCircuitBreaker(circuitBreaker, testClient, PATH_200);
            Resilience4jTestHelper.getCircuitBreakerStatus(">>>>>>>>>> end call " + count.incrementAndGet(), circuitBreaker);
        }
        assertEquals(CircuitBreaker.State.CLOSED.name(), circuitBreaker.getState().name());
    }

    @Test
    public void When_Test_CircuitBreaker_Expect_Open() {
        circuitBreakerWithTags = circuitBreakerRegistry.circuitBreaker("circuitBreakerWithTags", circuitBreakerConfig, HashMap.of("resilience4jTest", "When_CircuitBreaker_Expect_Open"));
        Resilience4jTestHelper.circuitBreakerEventListener(circuitBreakerWithTags);

        AtomicInteger count = new AtomicInteger();
        for (int i = 0; i < 10; i++) {
            Resilience4jTestHelper.recordResponseToCircuitBreaker(circuitBreakerWithTags, testClient, PATH_400);
            Resilience4jTestHelper.getCircuitBreakerStatus(">>>>>>>>>> end call " + count.incrementAndGet(), circuitBreakerWithTags);
        }
        assertEquals(CircuitBreaker.State.OPEN.name(), circuitBreakerWithTags.getState().name());
    }

    @Test
    public void When_Test_CircuitBreaker_Expect_SlowCall() throws Throwable {
        AtomicInteger count = new AtomicInteger();
        for (int i = 0; i < 10; i++) {
            circuitBreaker.executeCheckedSupplier(() -> {
                Resilience4jTestHelper.recordSlowCallResponseToCircuitBreaker(circuitBreaker, testClient, PATH_408);
                return null;
            });
            Resilience4jTestHelper.getCircuitBreakerStatus(">>>>>>>>>> end call " + count.incrementAndGet(), circuitBreaker);
        }
        assertEquals(CircuitBreaker.State.OPEN.name(), circuitBreaker.getState().name());
    }

    @Test
    public void When_Test_CircuitBreaker_Expect_Fallback() {
        AtomicInteger count = new AtomicInteger();
        for (int i = 0; i < 20; i++) {
            String path = PATH_500;
            CheckedFunction0<String> response =
                    circuitBreaker.decorateCheckedSupplier(() -> Resilience4jTestHelper.responseToCircuitBreaker(circuitBreaker, testClient, path));
            Try<String> result = Try.of(response).map(val -> {
                Resilience4jTestHelper.getCircuitBreakerStatus(">>>>>>>>>> call success " + count.incrementAndGet(), circuitBreaker);
                return val;
            }).recover(CallNotPermittedException.class, throwable -> {
                Resilience4jTestHelper.getCircuitBreakerStatus(">>>>>>>>>> open CircuitBreaker " + count.incrementAndGet(), circuitBreaker);
                return "hit CallNotPermittedException";
            }).recover(throwable -> {
                Resilience4jTestHelper.getCircuitBreakerStatus(">>>>>>>>>> call fallback " + count.incrementAndGet(), circuitBreaker);
                return "hit fallback";
            });
            log.info(">>>>>>>>>> result:{}", result.get());
            if (count.get() > 10) {
                assertEquals("hit CallNotPermittedException", result.get());
            }
        }
    }

    @Test
    public void When_Test_CircuitBreaker_With_Bulkhead_Expect_Hit_BulkheadFullException() {
        AtomicInteger count = new AtomicInteger();
        ExecutorService executorService = Executors.newFixedThreadPool(50);
        Bulkhead bulkhead1 = bulkheadRegistry.bulkhead("bulkhead1",
                BulkheadConfig
                        .custom()
                        .maxConcurrentCalls(20)
                        .maxWaitDuration(Duration.ofMillis(100))
                        .build());

        Resilience4jTestHelper.bulkheadEventListener(bulkhead1);
        for (int i = 0; i < 100; i++) {
            if (bulkhead1.tryAcquirePermission()) {
                log.info(">>>>>>>>>> acquire permission {}", count.incrementAndGet());
                Future<String> futureStr = executorService.submit(() -> Resilience4jTestHelper.responseToBulkhead(bulkhead1, testClient, PATH_200));
                Try.of(futureStr::get).andThen(val -> log.info(">>>>>>>>>> success {}: {}", count.get(), val)).recover(throwable -> {
                    if (throwable instanceof ExecutionException) {
                        Throwable cause = (ExecutionException) throwable.getCause();
                        if (cause instanceof BulkheadFullException) {
                            log.info(">>>>>>>>>> BulkheadFullException {}: {}", count.get(), throwable.getMessage());
                        } else {
                            log.info(">>>>>>>>>> ExecutionException {}: {}", count.get(), throwable.getMessage());
                        }
                    }
                    return "hit ExecutionException";
                });
                if (releasePermission()) {
                    bulkhead1.onComplete();
                    log.info("---------- release permission");
                }
                Resilience4jTestHelper.getBulkheadStatus(")))))))))) ", bulkhead1);
            } else {
                log.info(">>>>>>>>>> tryAcquirePermission false {}", count.incrementAndGet());
                continue;
            }
        }
        executorService.shutdown();
    }

    @Test
    public void When_Test_CircuitBreaker_Expect_Hit_RateLimiter() throws Exception {
        AtomicInteger count = new AtomicInteger();
        ExecutorService executorService = Executors.newFixedThreadPool(50);
        String path = expectError() ? PATH_500 : PATH_200;
        for (int i = 0; i < 100; i++) {
            Future<String> futureStr = executorService.submit(() -> Resilience4jTestHelper.responseToRateLimiter(rateLimiter, testClient, path));
            try {
                Future<String> stringFuture = rateLimiter.executeCallable(() -> futureStr);
                Try.of(stringFuture::get).andThen(val -> {
                    log.info(">>>>>>>>>> success {}: {}", count.incrementAndGet(), val);
                }).recover(throwable -> {
                    log.info(">>>>>>>>>> exception {}: {}", count.incrementAndGet(), throwable.getMessage());
                    return "hit fallback";
                });
                Resilience4jTestHelper.getRateLimiterStatus(")))))))))) ", rateLimiter);
            } catch (RequestNotPermitted exception) {
                assertEquals("RateLimiter 'resilience4jTest' does not permit further calls", exception.getMessage());
            }
        }
        executorService.shutdown();
    }

    @Test
    public void When_Test_CircuitBreaker_Expect_Retry() {
        AtomicInteger count = new AtomicInteger();
        for (int i = 0; i < 30; i++) {
            String path = expectError() ? PATH_200 : PATH_400;
            Callable<String> response = Retry.decorateCallable(retry, () -> Resilience4jTestHelper.responseToRetry(testClient, path));
            Try.of(response::call).andThen(val -> log.info(">>>>>>>>>> result {}: {}", count.incrementAndGet(), val));
            Resilience4jTestHelper.getRetryStatus("))))))))))", retry);
        }
    }

    @Test
    public void When_Test_CircuitBreaker_Expect_Timeout() {
        AtomicInteger count = new AtomicInteger();
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 30; i++) {
            String path = expectError() ? PATH_408 : PATH_200;
            Future<String> futureStr =
                    executorService.submit(() -> Resilience4jTestHelper.responseToTimeLimiter(timeLimiter, circuitBreaker, testClient, path));
            Callable<String> stringCallable = timeLimiter.decorateFutureSupplier(() -> futureStr);
            Callable<String> response = circuitBreaker.decorateCallable(stringCallable);
            Try.of(response::call).andThen(val -> log.info(">>>>>>>>>> success {} {}", count.incrementAndGet(), val))
                .recover(CallNotPermittedException.class, throwable -> {
                Resilience4jTestHelper.getCircuitBreakerStatus(">>>>>>>>>> open CircuitBreaker " + count.incrementAndGet(), circuitBreaker);
                return "hit CircuitBreaker";
            }).recover(throwable -> {
                Resilience4jTestHelper.getCircuitBreakerStatus(">>>>>>>>>> call fallback " + count.incrementAndGet(), circuitBreaker);
                log.error(">>>>>>>>>> fallback:{}", throwable.getMessage());
                return "hit Fallback";
            });
        }
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


}
