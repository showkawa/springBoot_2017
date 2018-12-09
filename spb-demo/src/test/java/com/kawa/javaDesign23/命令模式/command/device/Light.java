package com.kawa.javaDesign23.命令模式.command.device;
//灯
public class Light {

    private String local;

    public Light(String local) {
        this.local = local;
    }

    public void On(){
        System.out.println(local+ "turn on");
    }

    public void Off(){
        System.out.println(local +"turn off");
    }
}
