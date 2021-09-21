package com.kawa.mutilthread.forkjoin.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ForkJoinPool;

@Configuration
public class ForkJoinConfiguration {

    @Bean
    public ForkJoinPool getForkJoinPool(){
        return new ForkJoinPool();
    }
}
