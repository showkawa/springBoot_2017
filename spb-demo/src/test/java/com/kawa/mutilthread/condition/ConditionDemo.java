package com.kawa.mutilthread.condition;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
public class ConditionDemo {
    private static Lock lock = new ReentrantLock();
    private static Condition condition = lock.newCondition();

    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(ConditionDemo::run);
        thread.start();
        try {
            Thread.sleep(1000);
        } catch (Exception e) {  }
        lock.lock();
        // 唤醒
        condition.signal();
        lock.unlock();
        log.info("    === {}     ===: {} 33333",Thread.currentThread().getName());
    }

    private static void run() {
        lock.lock();
        try {
            log.info("=== {} ===: {} 11111", Thread.currentThread().getName());
            // 等待
            condition.await();
            log.info("=== {} ===: {} 22222", Thread.currentThread().getName());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
