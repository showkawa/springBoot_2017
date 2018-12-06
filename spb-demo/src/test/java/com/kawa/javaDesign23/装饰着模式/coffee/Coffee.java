package com.kawa.javaDesign23.装饰着模式.coffee;

import com.kawa.javaDesign23.装饰着模式.Drink;

public class Coffee extends Drink {
    @Override
    public float cost() {
        return super.getPrice();
    }
}
