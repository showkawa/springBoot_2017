package com.kawa.javaDesign23.适配器模式;

import com.kawa.javaDesign23.适配器模式.turkey.WildTurkey;
import org.junit.Test;

public class TestAdapter {

    @Test
    public void test(){
        WildTurkey wildTurkey = new WildTurkey();
        wildTurkey.gobble();
        wildTurkey.fly();

        TurkeyAdapter turkeyAdapter = new TurkeyAdapter(wildTurkey);
        turkeyAdapter.quack();
        turkeyAdapter.fly();

    }
}
