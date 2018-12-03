package com.kawa.javaDesign23.观察者模式;

import java.util.Observable;
import java.util.Observer;

public class CurrentConditions  implements Observer {

    private float temperatrue;
    private float pressure;
    private float humidity;

    @Override
    public void update(Observable o, Object arg) {
        this.temperatrue = ((WeatherData.Data)(arg)).temperatrue;
        this.pressure = ((WeatherData.Data)(arg)).pressure;
        this.humidity = ((WeatherData.Data)(arg)).humidity;
        display();
    }

    public void display(){
        System.out.println("Today 温度：" + temperatrue + Math.random());
        System.out.println("Today 气压：" + pressure + Math.random());
        System.out.println("Today 湿度：" + humidity + Math.random());
    }
}
