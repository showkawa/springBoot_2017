package com.kawa.javaDesign23.策略模式;

import com.kawa.javaDesign23.策略模式.behavior.FlyBehavior;

public class GoodFly  implements FlyBehavior {
    @Override
    public void fly() {
        System.out.println("飞的好的。。。");
    }
}
