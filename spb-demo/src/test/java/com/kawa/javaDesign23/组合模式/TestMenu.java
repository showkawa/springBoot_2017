package com.kawa.javaDesign23.组合模式;


import org.junit.jupiter.api.Test;

public class TestMenu {

    @Test
    public void test(){
        Print  p = new Print();
        MorningMenu morningMenu = new MorningMenu();
        DinnerMenu dinnerMenu = new DinnerMenu();
        p.addComponent(morningMenu);
        p.addComponent(dinnerMenu);
        p.printMenu();


    }
}
