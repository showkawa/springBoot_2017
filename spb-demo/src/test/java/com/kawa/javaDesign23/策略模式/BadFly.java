package com.kawa.javaDesign23.策略模式;

import com.kawa.javaDesign23.策略模式.behavior.FlyBehavior;
//飞行的行为
public class BadFly  implements FlyBehavior {
    @Override
    public void fly() {
        System.out.println("飞的不好的。。。。");
    }
}
