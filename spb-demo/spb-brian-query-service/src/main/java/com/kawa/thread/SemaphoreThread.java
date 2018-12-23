package com.kawa.thread;


import java.util.Random;
import java.util.concurrent.Semaphore;

/**
 * Semaphore是一种基于计数的信号量。它可以设定一个阈值，基于此，多个线程竞争获取许可信号，做自己的申请后归还，超过阈值后，线程申请许可信号将会被阻塞。Semaphore可以用来构建一些对象池，资源池之类的，比如数据库连接池，我们也可以创建计数为1的Semaphore，将其作为一种类似互斥锁的机制，这也叫二元信号量，表示两种互斥状态。它的用法如下：
 * availablePermits函数用来获取当前可用的资源数量
 * wc.acquire(); //申请资源
 * wc.release();// 释放资源
 */
public class SemaphoreThread {
    /**
     * 需求: 商城只有4个miniKTV，但是有13对情侣要去miniKTV，那怎么办？
     * 假设13对情侣的编号分别为1-13，并且1号先到唱K，13号最后唱K。
     * 那么1-4号来的时候必然有可用miniKTV，顺利享受KTV服务，5号来的时候需要看看前面4号是否有人出来了，如果有人出来，进去，否则等待。
     * 同样的道理，6-13号也需要等待正在享受miniKTV服务的人出来后才能进去，并且谁先进去这得看等待的人是否有素质，是否能遵守先来先上的规则。
     */


    public static void main(String[] args) {
        //初始化4个KTV
        Semaphore semaphore = new Semaphore(4);
        for (int i =1; i<= 13;i++){
            new Lovers(i+"",semaphore).start();
        }
    }

     static class Lovers extends Thread {

          private String name;
          private Semaphore sp;

        public Lovers(String name, Semaphore sp) {
            this.name = name;
            this.sp = sp;
        }

        @Override
        public synchronized void start() {
            super.start();
        }

        @Override
        public void run() {
            int availablePermits = sp.availablePermits();

            if(availablePermits > 0){
                System.out.println("情侣 " + this.name + " 有空余的KTV");
            }
            else{
                System.out.println("情侣 " + this.name + " 没有有空余的KTV");
            }
            try {
                sp.acquire();
                System.out.println("情侣 " + this.name + " 进入KTV享受服务");
                //模拟正在享受KTV服务
                Thread.sleep(new Random().nextInt(5000));
                System.out.println("情侣 " + this.name + " 离开KTV结束服务");
                sp.release();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}
