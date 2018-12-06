package com.kawa.javaDesign23.装饰着模式;

import com.kawa.javaDesign23.装饰着模式.coffee.Decaf;
import com.kawa.javaDesign23.装饰着模式.coffee.LongBlack;
import com.kawa.javaDesign23.装饰着模式.decorator.Chocolate;
import com.kawa.javaDesign23.装饰着模式.decorator.Milk;
import com.kawa.javaDesign23.装饰着模式.decorator.Soy;
import org.junit.Test;

public class TestDrink {


    @Test
    public void test(){
        Drink order;
        order = new Decaf();
        System.out.println("order1 price=> " +order.cost());
        System.out.println("order1 content=> " +order.getDescription());
        order = new LongBlack();
        order = new Milk(order);
        order= new Chocolate(order);
        order = new Soy(order);
        System.out.println("order2 price=> " +order.cost());
        System.out.println("order2 content=> " +order.getDescription());
    }


}
