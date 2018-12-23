package com.kawa.pojo;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Worker {
    private String wName;
    private String wNumber;
    private boolean wFlag = false;
    Lock lock = new ReentrantLock();

    public Lock getLock() {
        return lock;
    }

    public void setLock(Lock lock) {
        this.lock = lock;
    }

    public String getwName() {
        return wName;
    }

    public void setwName(String wName) {
        this.wName = wName;
    }

    public String getwNumber() {
        return wNumber;
    }

    public void setwNumber(String wNumber) {
        this.wNumber = wNumber;
    }

    public boolean getwFlag() {
        return wFlag;
    }

    public void setwFlag(boolean wFlag) {
        this.wFlag = wFlag;
    }

}