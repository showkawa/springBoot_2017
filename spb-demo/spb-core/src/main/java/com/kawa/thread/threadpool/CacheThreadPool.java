package com.kawa.thread.threadpool;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 可缓存的线程池 可以重复利用
 *newCachedThreadPool创建一个可缓存线程池，如果线程池长度超过处理需要，
 * 可灵活回收空闲线程，若无可回收，则新建线程。
 */
public class CacheThreadPool {

    public static void main(String[] args) {
        ExecutorService threadPool = Executors.newCachedThreadPool();

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
