package com.kawa.thread.threadpool;


import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 自定义线程池
 */
public class DefinedThreadPool {
    public static void main(String[] args) throws InterruptedException {
        ThreadPoolExecutor pool = new ThreadPoolExecutor(1,2,0L,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(3));
            pool.execute(new ThreadClass("任务1"));
            pool.execute(new ThreadClass("任务2"));
            pool.execute(new ThreadClass("任务3"));
            pool.execute(new ThreadClass("任务4"));
            pool.execute(new ThreadClass("任务5"));

            pool.shutdown();
    }

}

class ThreadClass extends  Thread{
    private  String name;
    public ThreadClass(String name) {
        this.name = name;
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() +" <> "+ this.name);
    }
}
