package com.kawa.javaDesign23.策略模式;

import com.kawa.javaDesign23.策略模式.behavior.QuackBehavior;

public class GeGeQuack implements QuackBehavior {
    @Override
    public void quack() {
        System.out.println("咯咯叫的。。。");
    }
}
