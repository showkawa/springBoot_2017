package com.kawa.javaDesign23.观察者模式;

import org.junit.Test;

public class TestObservable {

    @Test
    public void test (){
        CurrentConditions cc = new CurrentConditions();
        FutureConditions fc = new FutureConditions();
        WeatherData wd = new WeatherData();
        wd.addObserver(cc);
        wd.addObserver(fc);
        wd.setData(10,20,30);

    }
}
