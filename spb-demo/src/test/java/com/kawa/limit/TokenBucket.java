package com.kawa.limit;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 令牌桶算法
 */
@Slf4j
public class TokenBucket {
    private final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(5);

    // 桶容量
    public  int capacity = 1000;
    
    // 当前桶中请求数
    public int curretToken = 0;

    // 恒定的速率放入令牌
    private final int tokenCount = 200;

    public void doLimit() {
        scheduledExecutorService.scheduleWithFixedDelay(() -> {
            
            new Thread( () -> {
                if(curretToken >= capacity) {
                    log.info(" =====桶中的令牌已经满了===== ");
                    curretToken = capacity;
                } else {
                    if((curretToken+tokenCount) >= capacity){
                      log.info(" 当前桶中的令牌数:{},新进入的令牌将被丢弃的数: {}",curretToken,(curretToken+tokenCount-capacity));
                      curretToken = capacity;
                  } else {
                      curretToken += tokenCount;
                  }
                }
            }).start();

            new Thread( () -> {
                final int requestCount = (int) ((Math.random() * 200) + 50);
                if(requestCount >= curretToken){
                    log.info(" 当前请求数:{},桶中令牌数: {},将被丢弃的请求数:{}",requestCount,curretToken,(requestCount - curretToken));
                    curretToken = 0;
                } else {
                    log.info(" 当前请求数:{},桶中令牌数: {}",requestCount,curretToken);
                    curretToken -= requestCount;
                }
            }).start();
        }, 0, 500, TimeUnit.MILLISECONDS);
    }

    public static void main(String[] args) {
        new TokenBucket().doLimit();
    }
    
}