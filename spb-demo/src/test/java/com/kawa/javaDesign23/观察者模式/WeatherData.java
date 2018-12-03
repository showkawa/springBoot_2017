package com.kawa.javaDesign23.观察者模式;

import java.util.Observable;

/**
 * 观察者模式  模拟推送天气信息
 */
public class WeatherData  extends Observable {

    private float temperatrue;
    private float pressure;
    private float humidity;

    public float getTemperatrue() {
        return temperatrue;
    }

    public float getPressure() {
        return pressure;
    }

    public float getHumidity() {
        return humidity;
    }

    public void dataChange() {
        this.setChanged();
        this.notifyObservers(new Data(getTemperatrue(),getPressure(),getHumidity()));
    }

    public void setData(float temperatrue,float pressure, float humidity){
        this.temperatrue = temperatrue;
        this.pressure =pressure;
        this.humidity = humidity;
        dataChange();
    }

    public class Data {
        public float temperatrue;
        public float pressure;
        public float humidity;

        public Data(float temperatrue, float pressure, float humidity) {
            this.temperatrue = temperatrue;
            this.pressure = pressure;
            this.humidity = humidity;
        }
    }
}
