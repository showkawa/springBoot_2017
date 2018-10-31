package com.kawa.thread.lock;

import java.util.concurrent.locks.ReentrantLock;

/**
 *锁作为并发共享数据，保证一致性的工具，在JAVA平台有多种实现(如 synchronized 和 ReentrantLock等等 ) 。
 * 这些已经写好提供的锁为我们开发提供了便利。
 * 重入锁，也叫做递归锁，指的是同一线程 外层函数获得锁之后 ，内层递归函数仍然有获取该锁的代码，但不受影响。
 * 在JAVA环境下 ReentrantLock 和synchronized 都是 可重入锁
 */
public class ReentrantLockThread {

    public static void main(String[] args) {
        /*SynchronizedLock sl = new SynchronizedLock();
        new Thread(sl).start();
        new Thread(sl).start();
        new Thread(sl).start();
        new Thread(sl).start();
        new Thread(sl).start();*/

        ReentrantLLock rll = new ReentrantLLock();
        new Thread(rll).start();
        new Thread(rll).start();
        new Thread(rll).start();
        new Thread(rll).start();
        new Thread(rll).start();
    }




    static class SynchronizedLock  implements  Runnable{

        public synchronized void get(){
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName()+ " ===> get()");
            set();
        }

        public  synchronized void set(){
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName()+ " ===> set()");

        }
        

        @Override
        public void run() {
            get();
        }

    }

    static class ReentrantLLock  implements  Runnable{

        ReentrantLock rl = new ReentrantLock();

        public void get(){
            rl.lock();
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName()+ " ===> get()");
            set();
            rl.unlock();
        }

        public void set(){
            rl.lock();
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName()+ " ===> set()");
            rl.unlock();
        }

        @Override
        public void run() {
            get();
        }
    }

}
/*"
Thread-0 ===> get()
Thread-1 ===> get()
Thread-2 ===> get()
Thread-3 ===> get()
Thread-4 ===> get()
Thread-0 ===> set()
Thread-1 ===> set()
Thread-2 ===> set()
Thread-4 ===> set()
Thread-3 ===> set()

Process finished with exit code 0
添加重入锁后的结果======》

"
Thread-0 ===> get()
Thread-0 ===> set()
Thread-4 ===> get()
Thread-4 ===> set()
Thread-3 ===> get()
Thread-3 ===> set()
Thread-2 ===> get()
Thread-2 ===> set()
Thread-1 ===> get()
Thread-1 ===> set()

Process finished with exit code 0
*/