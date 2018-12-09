package com.kawa.javaDesign23.命令模式.command.device;
//音响
public class Stereo {
    static int volume = 0;

    public void On(){
        System.out.println("音响 turn on");
    }

    public void Off() {
        System.out.println("音响 turn off");
    }

    public void SetMusic(){
        System.out.println("音响 choose music");
    }

    public void SteVol(int vol){
        volume = vol;
    }

    public int GetVol(){
        return volume;
    }

    public  void Start() {
        System.out.println("Stereo is start");
    }

    public void Stop() {
        System.out.println("Stereo is stop");
    }
}
