package com.kawa.mutilthread.forkjoin.controller;


import com.kawa.mutilthread.forkjoin.service.AutomationTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

@RestController
@Slf4j
public class ForkJoinTestController {

    @Autowired
    public ForkJoinPool forkJoinPool;

    @GetMapping("/getForkJoinResult")
    public Flux<Map<String, String>> getForkJoinResult(@RequestParam(value = "country", defaultValue = "CN,HK,JP,KR,SG,TH,TW,ER") String country)
            throws ExecutionException, InterruptedException {
        String[] countries = country.split(",");
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        AutomationTask computeTask = new AutomationTask(Arrays.stream(countries).collect(Collectors.toList()), 0, countries.length - 1);
        Future<List<Map<String, String>>> results = forkJoinPool.submit(computeTask);
        if(computeTask.isCompletedAbnormally()){
            log.info("<><><><><><><><><><> automationTask exception: {}", computeTask.getException());
        }

        List<Map<String, String>> res = results.get();
        log.info(">>>>>>>>>>>>>>>>>>>>result size : {}", res.size());
        Flux<Map<String, String>> mapFlux = Flux.fromIterable(res);
        stopWatch.stop();
        log.info(">>>>>>>>>>>total handle time: {} ms", stopWatch.getTotalTimeMillis());
        return mapFlux;
    }

}
