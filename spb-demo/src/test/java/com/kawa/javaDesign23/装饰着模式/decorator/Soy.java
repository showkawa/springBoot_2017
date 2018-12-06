package com.kawa.javaDesign23.装饰着模式.decorator;

import com.kawa.javaDesign23.装饰着模式.Drink;

public class Soy extends Decorator{
    public Soy(Drink drink) {
        super(drink);
        super.setDescription("Soy");
        super.setPrice(4.0f);
    }
}
