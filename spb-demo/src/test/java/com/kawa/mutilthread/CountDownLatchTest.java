package com.kawa.mutilthread;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * CountDownLatch 类位于java.util.concurrent包下，利用它可以实现类似计数器的功能。
 * 比如有一个任务A，它要等待其他4个任务执行完毕之后才能执行，此时就可以利用CountDownLatch来实现这种功能了。
 *      CountDownLatch是通过一个计数器来实现的，计数器的初始值为线程的数量。
 *      每当一个线程完成了自己的任务后，计数器的值就会减1。当计数器值到达0时，
 *      它表示所有的线程已经完成了任务，然后在闭锁上等待的线程就可以恢复执行任务。
 */
@Slf4j
public class CountDownLatchTest {
    public static void main(String[] args) {
        CountDownLatch cdl = new CountDownLatch(3);

        new Thread(() -> {
            log.info(Thread.currentThread().getName() + " <> 飞机目的地信息同步成功");
            cdl.countDown();
        },"F-desc").start();

        new Thread(() -> {
            log.info(Thread.currentThread().getName() + " <> 飞机雷达信息更新成功");
            cdl.countDown();
        },"F-leida").start();

      new Thread(() -> {
            try{
                TimeUnit.SECONDS.sleep(2);
            }catch(Exception e){

            }
            log.info(Thread.currentThread().getName() + " <> 飞机收到调度中心着路指令");
            cdl.countDown();
        },"F-job").start();

        // CountDownLatch 计数减去为0之前，下面的程序将一直被阻塞
        try{
            cdl.await();
        }catch(Exception e){

        }
        log.info(Thread.currentThread().getName() + " <> 飞机准备降落B8跑到");
    }
}