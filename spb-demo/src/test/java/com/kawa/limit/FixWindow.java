package com.kawa.limit;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 固定窗口
 */
@Slf4j
public class FixWindow {

    private final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(5);

    private final int limit = 100;

    private AtomicInteger currentCircleRequestCount = new AtomicInteger(0);

    private AtomicInteger timeCircle = new AtomicInteger(0);

    private void doFixWindow() {
        scheduledExecutorService.scheduleWithFixedDelay(() -> {
            log.info(" 当前时间窗口，第 {} 秒 ", timeCircle.get());
            if(timeCircle.get() >= 60) {
                timeCircle.set(0);
                currentCircleRequestCount.set(0);
                log.info(" =====进入新的时间窗口===== ");
            }
            if(currentCircleRequestCount.get() > limit) {
                log.info("触发限流策略，当前窗口累计请求数 : {}", currentCircleRequestCount);
            } else {
                final int requestCount = (int) ((Math.random() * 5) + 1);
                log.info("当前发出的 ==requestCount== : {}", requestCount);
                currentCircleRequestCount.addAndGet(requestCount);
            }
           timeCircle.incrementAndGet();
        }, 0, 1, TimeUnit.SECONDS);
    }

    public static void main(String[] args) {
        new FixWindow().doFixWindow();
    }




    
}