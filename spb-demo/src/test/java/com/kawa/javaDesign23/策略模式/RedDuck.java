package com.kawa.javaDesign23.策略模式;

public class RedDuck extends Duck {

    public RedDuck() {
        flyBehavior =  new BadFly();
        quackBehavior = new GeGeQuack();
    }

    @Override
    public void display() {
        System.out.println("红色的鸭子。。。。");
    }
}
