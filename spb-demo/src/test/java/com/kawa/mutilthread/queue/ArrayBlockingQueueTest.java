package com.kawa.mutilthread.queue;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;

/**
 * ArrayBlockingQueue是一个有边界的阻塞队列，它的内部实现是一个数组。有边界的意思是它的容量是有限的，
 * 我们必须在其初始化的时候指定它的容量大小，容量大小一旦指定就不可改变。
 * ArrayBlockingQueue是以先进先出的方式存储数据，最新插入的对象是尾部，最新移出的对象是头部。
 */
public class ArrayBlockingQueueTest {

    /**
     * add()
     在不超出队列长度的情况下插入元素，可以立即执行，成功返回true，如果队列满了就抛出异常
        * 其底层实现的是offer方法，不会阻塞
        * public boolean add(E e) {
        *    if (offer(e))
        return true;
        else
        throw new IllegalStateException("Queue full");
        }
        */

    /**
     * offer（）方法：
     * 在不超出队列长度的情况下插入元素的时候则可以立即在队列的尾部插入指定元素
     * 成功时返回true，如果此队列已满，则返回false。
     * 不会阻塞
     *
     */
    
    /**
     * put()方法：
     * 插入元素的时候，如果队列满了就进行等待，直到队列可用
     */

    /**
     *  remove（）方法取值：
     *  底层是用到了poll()方法，检索并且删除返回队列头的元素
     *  与poll()方法不同的是，元素没有是进行抛异常NoSuchElementException
     *public E remove() {
         E x = poll();
            if (x != null)
            return x;
            else
            throw new NoSuchElementException();
        }
        */

    /**
     * poll()
     * 检索并且删除返回队列头的元素,有就返回没有就返回null
     */

    /**
     * take()
     * 检索并且删除返回队列头的元素,如果元素没有会一直等待，有就返回
     */

    /**
     * peek()
     * 检索但不移除此队列的头部;如果此队列为空，则返回null。
     * 返回头部元素
     */

    public static void main(String[] args) {

        CountDownLatch countDownLatch = new CountDownLatch(1);
        // 初始化阻塞队列容量为10 10个茅坑
        ArrayBlockingQueue<String> arrayBlockingQueue = new ArrayBlockingQueue<>(10);
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
                    arrayBlockingQueue.put(passenger);
                    System.out.println(passenger + " 进入厕所");
                }catch(Exception e){}
            }
        }).start();
        
        new Thread(() -> {
            while (true){
                if(arrayBlockingQueue.isEmpty()){
                    continue;
                }
                try{
                    System.out.println(arrayBlockingQueue.take() + " 离开厕所");
                }catch(Exception e){}
            }
        }).start();
    }
}