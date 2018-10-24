package com.kawa.pojo;

public class Worker {
    private String wName;
    private String wNumber;
    private boolean wFlag = false;

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

    @Override
    public String toString() {
        return "Worker{" +
                "wName='" + wName + '\'' +
                ", wNumber='" + wNumber + '\'' +
                ", wFlag=" + wFlag +
                '}';
    }
}