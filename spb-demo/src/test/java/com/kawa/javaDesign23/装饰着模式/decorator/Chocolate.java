package com.kawa.javaDesign23.装饰着模式.decorator;

import com.kawa.javaDesign23.装饰着模式.Drink;

public class Chocolate extends Decorator{
    public Chocolate(Drink drink) {
        super(drink);
        super.setDescription("Chocolate");
        super.setPrice(2.0f);
    }
}
