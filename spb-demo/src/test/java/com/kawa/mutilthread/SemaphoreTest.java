package com.kawa.mutilthread;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * Semaphore是一种基于计数的信号量。它可以设定一个阈值，基于此，多个线程竞争获取许可信号，做自己的申请后归还，
 * 超过阈值后，线程申请许可信号将会被阻塞。Semaphore可以用来构建一些对象池，资源池之类的，比如数据库连接池，
 * 我们也可以创建计数为1的Semaphore，将其作为一种类似互斥锁的机制，这也叫二元信号量，表示两种互斥状态。它的用法如下：
 * availablePermits函数用来获取当前可用的资源数量 wc.acquire(); //申请资源 wc.release();// 释放资源
 * 
 */
public class SemaphoreTest {

    public static void main(String[] args) {
            Semaphore sh = new Semaphore(20);

            for(int i =0;i<500;i++){
                new RunWayThread(sh, i+"").start();
            }
    }
}


@Slf4j
class RunWayThread extends Thread {
    private Semaphore sh;
    private String flight;

    public RunWayThread(Semaphore sh, String flight) {
        this.sh = sh;
        this.flight = flight;
    }

    @Override
    public void run() {
        if(sh.availablePermits() >0){
            log.info(Thread.currentThread().getName() +" <> 飞行跑道未占满，航班 ["+flight+"] 准备起飞" );
        } else {
            log.info(Thread.currentThread().getName() +" <> 飞行跑道已占满，航班 ["+flight+"] 等待调度" );
        }
        
        try {
            sh.acquire();
            TimeUnit.SECONDS.sleep(1);
            log.info(Thread.currentThread().getName() +" <> 航班 ["+flight+"] 起飞了" );
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally{
            sh.release();
        }
    }
}