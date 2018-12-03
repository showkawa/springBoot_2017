package com.kawa.javaDesign23.策略模式;

public class BlueDuck  extends Duck{

    public BlueDuck() {
        flyBehavior = new GoodFly();
        quackBehavior = new GaGaQuack();
    }

    @Override
    public void display() {
        System.out.println("蓝色的鸭子。。。。");
    }
}
