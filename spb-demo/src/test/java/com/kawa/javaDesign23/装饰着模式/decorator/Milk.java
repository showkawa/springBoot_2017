package com.kawa.javaDesign23.装饰着模式.decorator;

import com.kawa.javaDesign23.装饰着模式.Drink;

public class Milk extends Decorator{
    public Milk(Drink drink) {
        super(drink);
        super.setDescription("Milk");
        super.setPrice(3.0f);
    }
}
