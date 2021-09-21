package com.kawa.mutilthread.queue;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * LinkedBlockingQueue阻塞队列大小的配置是可选的，如果我们初始化时指定一个大小，它就是有边界的，如果不指定，它就是无边界的。
 * 说是无边界，其实是采用了默认大小为Integer.MAX_VALUE的容量 。它的内部实现是一个链表。和linkedBlockingQueue一样，
 * LinkedBlockingQueue 也是以先进先出的方式存储数据，最新插入的对象是尾部，最新移出的对象是头部。
 */
public class LinkedBlockingQueueTest {

    /**
     * 一、添加元素
        1、add 方法：如果队列已满，报java.lang.IllegalStateException: Queue full 错误
        2、offer 方法：如果队列已满，程序正常运行，只是不再新增元素
        3、put 方法：如果队列已满，阻塞

        二、取元素
        1、poll 方法：弹出队顶元素，队列为空时返回null
        2、peek 方法：返回队列顶元素，但顶元素不弹出，队列为空时返回null
        3、take 方法：当队列为空，阻塞
     */

    public static void main(String[] args) {

        CountDownLatch countDownLatch = new CountDownLatch(1);
        // 初始化阻塞队列容量为10 10个茅坑
        LinkedBlockingQueue<String> linkedBlockingQueue = new LinkedBlockingQueue<>(10);
        // 初始化乘客得容量 10000
        ConcurrentLinkedQueue<Integer> concurrentLinkedQueue = new ConcurrentLinkedQueue<>();
        int passengerIndex = 1;

        for (;;) {
            concurrentLinkedQueue.offer(passengerIndex);
            if (++passengerIndex > 10000) {
                System.out.println("10000乘客准备就位============================");
                countDownLatch.countDown();
                break;
            }
        }

       try{
           countDownLatch.await();
       }catch(Exception e){}
        new Thread(() -> {
            while (true) {
                if(concurrentLinkedQueue.isEmpty()){
                    continue;
                }
                String passenger = "乘客0" + concurrentLinkedQueue.poll();
                try{
                    linkedBlockingQueue.put(passenger);
                    System.out.println(passenger + " 进入厕所");
                }catch(Exception e){}
            }
        }).start();
        
        new Thread(() -> {
            while (true){
                if(linkedBlockingQueue.isEmpty()){
                    continue;
                }
                try{
                    System.out.println(linkedBlockingQueue.take() + " 离开厕所");
                }catch(Exception e){}
            }
        }).start();
    }
}