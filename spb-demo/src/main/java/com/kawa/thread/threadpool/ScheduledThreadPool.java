package com.kawa.thread.threadpool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * newScheduledThreadPool 创建一个定长线程池，支持定时及周期性任务执行
 */
public class ScheduledThreadPool {
    public static void main(String[] args) {
        //最多有三个线程在同事执行任务
        ExecutorService threadPool = Executors.newScheduledThreadPool(3);
        for(int i=1;i<=15;i++){
            int count =i;
            ((ScheduledExecutorService) threadPool).schedule(new Runnable() {
                @Override
                public void run() {
                    System.out.println("当前执行任务的线程是  "+ Thread.currentThread().getName() + "    [ i ="+ count + "]");
                }
            //3秒后线程开始执行
            },3,TimeUnit.SECONDS);
        }
    }


}
