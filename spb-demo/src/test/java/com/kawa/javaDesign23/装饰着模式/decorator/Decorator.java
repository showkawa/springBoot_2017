package com.kawa.javaDesign23.装饰着模式.decorator;

import com.kawa.javaDesign23.装饰着模式.Drink;

//装饰者
public class Decorator extends Drink {
    private Drink drink;

    public Decorator(Drink drink) {
        this.drink = drink;
    }

    @Override
    public float cost() {
        return super.getPrice()+drink.cost();
    }

    @Override
    public String getDescription() {
        return super.description+"-"+super.getPrice()+"+"+drink.getDescription();
    }
}
