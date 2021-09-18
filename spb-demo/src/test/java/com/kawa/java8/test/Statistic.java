package com.kawa.java8.test;

import java.util.List;

public class Statistic {
    List<StateGroup> stateGroups;

    public List<StateGroup> getStateGroups() {
        return stateGroups;
    }

    public void setStateGroups(List<StateGroup> stateGroups) {
        this.stateGroups = stateGroups;
    }

    public Statistic(List<StateGroup> stateGroups) {
        this.stateGroups = stateGroups;
    }

    public Statistic() {
    }
}
