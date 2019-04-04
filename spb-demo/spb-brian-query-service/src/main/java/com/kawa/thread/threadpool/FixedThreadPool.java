package com.kawa.thread.threadpool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * newFixedThreadPool 创建一个定长线程池，可控制线程最大并发数，超出的线程会在队列中等待。
 */
public class FixedThreadPool {

    public static void main(String[] args) {
        //最多有三个线程在同事执行任务
        ExecutorService threadPool = Executors.newFixedThreadPool(3);

        for(int i=1;i<=15;i++){
            int count =i;
            threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    System.out.println("当前执行任务的线程是  "+ Thread.currentThread().getName() + "    [ i ="+ count + "]");
                }
            });
        }

    }
}
