package com.kawa.limit;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 滑动窗口
 * 
 * 60s限流100次请求
 */
@Slf4j
public class RollingWindow {

    private final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(5);

    // 窗口跨度时间60s
    private int timeWindow = 60;

    // 限流100个请求
    private final int limit = 100;

    // 当前窗口请求数
    private AtomicInteger currentWindowRequestCount = new AtomicInteger(0);

    // 时间片段滚动次数
    private AtomicInteger timeCircle = new AtomicInteger(0);

    // 触发了限流策略后等待的时间
    private AtomicInteger waitTime = new AtomicInteger(0);

    // 在下一个窗口时，需要减去的请求数
    private int expiredRequest = 0;

    // 时间片段为5秒，每5秒统计下过去60秒的请求次数
    private final int slidingTime = 5;

    private ArrayBlockingQueue<Integer> slidingTimeValues = new ArrayBlockingQueue<>(11);

    public void rollingWindow() {
        scheduledExecutorService.scheduleWithFixedDelay(() -> {

            if (waitTime.get() > 0) {
                waitTime.compareAndExchange(waitTime.get(), waitTime.get() - slidingTime);
                log.info("=====当前滑动窗口===== 限流等待下一个时间窗口倒计时: {}s", waitTime.get());
                if (currentWindowRequestCount.get() > 0) {
                    currentWindowRequestCount.set(0);
                }
            } else {
                final int requestCount = (int) ((Math.random() * 10) + 7);
                if (timeCircle.get() < 12) {
                    timeCircle.incrementAndGet();
                }
                
            log.info("当前时间片段5秒内的请求数: {} ", requestCount);
            currentWindowRequestCount.addAndGet(requestCount);
            log.info("=====当前滑动窗口===== {}s 内请求数: {} ", timeCircle.get()*slidingTime , currentWindowRequestCount.get());

            if(!slidingTimeValues.offer(requestCount)){
                expiredRequest =  slidingTimeValues.poll();
                slidingTimeValues.offer(requestCount);
            } 

            if(currentWindowRequestCount.get() > limit) {
                // 触发限流
                log.info("=====当前滑动窗口===== 请求数超过100, 触发限流,等待下一个时间窗口 ");
                waitTime.set(timeWindow);
                timeCircle.set(0);
                slidingTimeValues.clear();
            } else {
                // 没有触发限流，滑动下一个窗口需要,移除相应的:在下一个窗口时，需要减去的请求数
                log.info("=====当前滑动窗口===== 请求数 <100, 未触发限流，当前窗口请求总数: {},即将过期的请求数：{}"
                        ,currentWindowRequestCount.get(), expiredRequest);
                currentWindowRequestCount.compareAndExchange(currentWindowRequestCount.get(), currentWindowRequestCount.get() - expiredRequest);
            }
        }   
        }, 5, 5, TimeUnit.SECONDS);
    }

    public static void main(String[] args) {
        new RollingWindow().rollingWindow();
    }
    

}