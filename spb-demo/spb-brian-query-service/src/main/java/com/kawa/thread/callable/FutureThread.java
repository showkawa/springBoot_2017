package com.kawa.thread.callable;

import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 */
public class FutureThread implements Runnable{

   private AtomicInteger count = new AtomicInteger();
  //  private int count = 1;

    @Override
    public void run() {
        while(true){
            if(count.get()>=100){
           // if(count>=100){
                break;
            }
            System.out.println(Thread.currentThread().getName()+" <> " + getCount());
        }
    }

    public  int getCount() {
        try {
            Thread.sleep(500);
        }catch (Exception e){
        }
       // return count++;
        return count.incrementAndGet();
    }

    public static void main(String[] args) {
        FutureThread futureThread = new FutureThread();
        new Thread(futureThread).start();
        new Thread(futureThread).start();

    }
}
