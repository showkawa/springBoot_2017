package com.kawa.javaDesign23.观察者模式;

import java.util.Observable;
import java.util.Observer;

public class FutureConditions implements Observer {
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
        System.out.println("明天 温度：" + temperatrue);
        System.out.println("明天 气压：" + pressure);
        System.out.println("明天 湿度：" + humidity);
    }
}
