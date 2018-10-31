package com.kawa.thread.threadpool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *  newSingleThreadExecutor 创建一个单线程化的线程池，它只会用唯一的工作线程来执行任务，
 *  保证所有任务按照指定顺序(FIFO, LIFO, 优先级)执行。
 *
 *  如果该线程因为异常而结束就新建一条线程来继续执行后续的任务
 */
public class SingleThreadExecutor {
    public static void main(String[] args) {
        ExecutorService threadPool = Executors.newSingleThreadExecutor();

        for (int i=1;i<= 20;i++) {
            int count = i;
            threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    System.out.println("当前执行任务的线程是  "+ Thread.currentThread().getName() + "    [ i ="+ count + "]");
                }
            });
        }
    }
}
