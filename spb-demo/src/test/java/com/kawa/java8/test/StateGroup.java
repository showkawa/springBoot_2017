package com.kawa.java8.test;

import java.util.List;

public class StateGroup {
    public  Group group;
    public List<State> states;

    public StateGroup() {
    }

    public StateGroup(Group group, List<State> states) {
        this.group = group;
        this.states = states;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public List<State> getStates() {
        return states;
    }

    public void setStates(List<State> states) {
        this.states = states;
    }
}
