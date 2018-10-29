package com.kawa.thread;

import java.util.concurrent.CountDownLatch;

/**
 *
 *  CountDownLatch类位于java.util.concurrent包下，利用它可以实现类似计数器的功能
 */
public class CountDownLatchThread {

    public static void main(String[] args) {
        System.out.println("等待子线程运行");
        CountDownLatch countDownLatch = new CountDownLatch(2);
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("子线程 " + Thread.currentThread().getName() + " start");
                    //每次执行CountDownLatch设置的值将会减1
                    countDownLatch.countDown();
                System.out.println("子线程 " + Thread.currentThread().getName() + " end");
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("子线程 " + Thread.currentThread().getName() + " start");
                    //每次执行CountDownLatch设置的值将会减1
                    countDownLatch.countDown();
                System.out.println("子线程 " + Thread.currentThread().getName() + " end");
            }
        }).start();

        try {
            //调用当前主线程为阻塞状态，当countDown结果为0时，当前的主线程从阻塞状态变为运行状态
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("子线程运行结束，主线程开始运行");
    }
}
