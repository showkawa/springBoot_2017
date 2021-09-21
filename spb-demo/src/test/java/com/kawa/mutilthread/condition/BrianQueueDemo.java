package com.kawa.mutilthread.condition;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class BrianQueueDemo {

    public static void main(String[] args) throws InterruptedException {
        BrianQueue<Integer> brianQueue = new BrianQueue<>(5);
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        executorService.execute(()->{
            Integer num = 0;
            while (true){
                try {
                    brianQueue.add(++num);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        executorService.execute(()->{
            while (true){
                try {
                    brianQueue.remove();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
