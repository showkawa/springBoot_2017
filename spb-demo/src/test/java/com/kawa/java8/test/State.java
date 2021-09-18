package com.kawa.java8.test;

public class State {
    public String stateName;
    public Integer count;

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public State() {
    }

    public State(String stateName, Integer count) {
        this.stateName = stateName;
        this.count = count;
    }
}
