package com.kawa.mutilthread.threadpool;

import java.util.Random;
import java.util.concurrent.*;

/**
 * ThreadPoolExecutor自定义线程池
 * 
    corePoolSize:指定了线程池中的线程数量，它的数量决定了添加的任务是开辟新的线程去执行，还是放到workQueue任务队列中去；
    maximumPoolSize:指定了线程池中的最大线程数量，这个参数会根据你使用的workQueue任务队列的类型，决定线程池会开辟的最大线程数量；
    keepAliveTime:当线程池中空闲线程数量超过corePoolSize时，多余的线程会在多长时间内被销毁；
    unit:keepAliveTime的单位
    workQueue:任务队列，被添加到线程池中，但尚未被执行的任务；它一般分为直接提交队列、有界任务队列、无界任务队列、优先任务队列几种；
    threadFactory:线程工厂，用于创建线程，一般用默认即可；
    handler:拒绝策略；当任务太多来不及处理时，如何拒绝任务；
 * 
 */
public class ThreadPoolExecutorTest {

    private static ExecutorService pool;

    public static void main(String[] args) {
        
        CountDownLatch cdl = new CountDownLatch(500);

        pool = new ThreadPoolExecutor(1, 20, 30, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(100));

        for(int i=0;i<500;i++){
            TaskThread tt = new TaskThread(i+1, cdl);
            try{
                int times = new Random().nextInt(9);
                tt.sleep(times);   
                pool.execute(tt);
                
            }catch(Exception e){}
        }

        try{
            cdl.await();
            pool.shutdown();
        }catch(Exception e){}
        
    }
}

class TaskThread extends Thread {

    private CountDownLatch countDownLatch;

    private int no;

    public TaskThread(int no, CountDownLatch countDownLatch){
        this.no = no;
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void run() {
        try{
            System.out.println(Thread.currentThread().getName() + " <> " + this.no);
            countDownLatch.countDown();
        }catch(Exception e){}
    }

}