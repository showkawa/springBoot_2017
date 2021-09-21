package com.kawa.limit;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 漏桶算法
 */
@Slf4j
public class LeakyBucket {
    private final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(5);

    // 桶容量
    public  int capacity = 1000;
    
    // 当前桶中请求数
    public int curretRequest = 0;

    // 每秒恒定处理的请求数
    private final int handleRequest = 100;

    public void doLimit() {
        scheduledExecutorService.scheduleWithFixedDelay(() -> {
            final int requestCount = (int) ((Math.random() * 200) + 50);
            if(capacity > requestCount){
                capacity -= requestCount;
                log.info("<><>当前1秒内的请求数:{}, 桶的容量:{}", requestCount, capacity);
                if(capacity <=0) {
                    log.info(" =====触发限流策略===== ");
                } else {
                    capacity += handleRequest;
                    log.info("<><><><>当前1秒内处理请求数:{}, 桶的容量:{}", handleRequest, capacity);
                }
            } else {
                log.info("<><><><>当前请求数:{}, 桶的容量:{},丢弃的请求数:{}", requestCount, capacity,requestCount-capacity);
                if(capacity <= requestCount) {
                    capacity = 0;
                }
                capacity += handleRequest;
                log.info("<><><><>当前1秒内处理请求数:{}, 桶的容量:{}", handleRequest, capacity);
            }
        }, 0, 1, TimeUnit.SECONDS);
    }

    public static void main(String[] args) {
        new LeakyBucket().doLimit();
    }
}