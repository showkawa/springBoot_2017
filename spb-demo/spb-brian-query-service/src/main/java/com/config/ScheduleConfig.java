package com.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 *  设置线程池大小
 *
 *   newScheduledThreadPool 创建一个定长线程池，支持定时及周期性任务执行
 */
@Configuration
public class ScheduleConfig implements SchedulingConfigurer {
    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setScheduler(Executors.newScheduledThreadPool(5));
    }

    @Bean
    public ThreadPoolTaskExecutor  brianThreadPool(){
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //核心线程数
        executor.setCorePoolSize(8);
        //最大线程数
        executor.setMaxPoolSize(16);
        //队列中最大的数
        executor.setQueueCapacity(8);
        //县城名称前缀
        executor.setThreadNamePrefix("brianThreadPool_");
        //rejectionPolicy：当pool已经达到max的时候，如何处理新任务
        //callerRuns：不在新线程中执行任务，而是由调用者所在的线程来执行
        //对拒绝task的处理策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        //线程空闲后最大的存活时间
        executor.setKeepAliveSeconds(60);
        //初始化加载
        executor.initialize();
        return executor;
    }
}
