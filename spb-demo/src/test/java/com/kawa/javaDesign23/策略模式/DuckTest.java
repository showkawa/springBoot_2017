package com.kawa.javaDesign23.策略模式;

import org.junit.Test;

public class DuckTest {

    @Test
    public void test(){
        Duck red = new RedDuck();
        Duck blue = new BlueDuck();
        red.display();
        red.Fly();
        red.Quack();
        blue.display();
        blue.Fly();
        blue.Quack();
        blue.setFlyBehavior(new BadFly());
        blue.display();
        blue.Fly();
        blue.Quack();
    }

}
