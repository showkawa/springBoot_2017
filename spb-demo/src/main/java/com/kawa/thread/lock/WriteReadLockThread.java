package com.kawa.thread.lock;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 相比Java中的锁(Locks in Java)里Lock实现，读写锁更复杂一些。假设你的程序中涉及到对一些共享资源的读和写操作，且写操作没有读操作那么频繁。
 * 在没有写操作的时候，两个线程同时读一个资源没有任何问题，所以应该允许多个线程能在同时读取共享资源。但是如果有一个线程想去写这些共享资源，
 * 就不应该再有其它线程对该资源进行读或写（也就是说：读-读能共存，读-写不能共存，写-写不能共存）。
 * 这就需要一个读/写锁来解决这个问题。Java5在java.util.concurrent包中已经包含了读写锁。
 */
public class WriteReadLockThread {


    public static void main(String[] args) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                for (int i = 0; i < 5; i++) {
                    WriteReadLockThread.put(i + "", i + "");
                }

            }
        }).start();
        new Thread(new Runnable() {

            @Override
            public void run() {
                for (int i = 0; i < 5; i++) {
                    WriteReadLockThread.get(i + "");
                }

            }
        }).start();

    }







    static Map<String, Object> mp = new HashMap<>();
    static ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    static Lock r = lock.readLock();
    static Lock w = lock.writeLock();

    public static final Object put(String attr,Object obj){
        w.lock();
        try {
            System.out.println("写操作开始：[ attr: "+  attr +", obj: " + obj);
            Object object = mp.put(attr, obj);
            System.out.println("写操作结束：[ attr: "+  attr +", obj: " + obj);
            System.out.println();
            return object;
        }catch (Exception e){

        }
        finally {
            w.unlock();
        }
        return obj;
    }

    public static final Object get(String attr){
        r.lock();
        try {
            System.out.println("读操作开始：[ attr: "+  attr);
            Object object = mp.get(attr);
            System.out.println("读操作结束：[ attr: "+  attr);
            System.out.println();
            return object;
        }catch (Exception e){

        } finally {
            r.unlock();
        }
        return attr;
    }

}
/*
* 写操作开始：[ attr: 0, obj: 0
写操作结束：[ attr: 0, obj: 0
写操作开始：[ attr: 1, obj: 1
写操作结束：[ attr: 1, obj: 1
写操作开始：[ attr: 2, obj: 2
写操作结束：[ attr: 2, obj: 2
写操作开始：[ attr: 3, obj: 3
写操作结束：[ attr: 3, obj: 3
写操作开始：[ attr: 4, obj: 4
写操作结束：[ attr: 4, obj: 4
读操作开始：[ attr: 0
读操作结束：[ attr: 0
读操作开始：[ attr: 1
读操作结束：[ attr: 1
读操作开始：[ attr: 2
读操作结束：[ attr: 2
读操作开始：[ attr: 3
读操作结束：[ attr: 3
读操作开始：[ attr: 4
读操作结束：[ attr: 4

Process finished with exit code 0*/