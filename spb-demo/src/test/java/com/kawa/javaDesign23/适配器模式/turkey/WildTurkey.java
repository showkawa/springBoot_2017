package com.kawa.javaDesign23.适配器模式.turkey;

public class WildTurkey implements Turkey {
    @Override
    public void gobble() {
        System.out.println("Go Go");
    }

    @Override
    public void fly() {
        System.out.println("turkey 飞飞");
    }
}
