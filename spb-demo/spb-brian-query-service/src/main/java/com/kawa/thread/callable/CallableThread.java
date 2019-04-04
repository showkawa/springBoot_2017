package com.kawa.thread.callable;


import java.util.concurrent.*;

/**
 *在Java中，创建线程一般有两种方式，一种是继承Thread类，一种是实现Runnable接口。然而，这两种方式的缺点是在线程任务执行结束后，无法获取执行结果。
 * 我们一般只能采用共享变量或共享存储区以及线程通信的方式实现获得任务结果的目的。
 * 不过，Java中，也提供了使用Callable和Future来实现获取任务结果的操作。Callable用来执行任务，产生结果，而Future用来获得结果。
 */
public class CallableThread {



    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService threadPool = Executors.newFixedThreadPool(3);
        Future<String> submit = threadPool.submit(new TaskCallable());

        System.out.println(submit.get());

        threadPool.shutdown();

    }
}


class TaskCallable implements Callable<String> {
    @Override
    public String call() throws Exception {
        return "AAA";
    }
}