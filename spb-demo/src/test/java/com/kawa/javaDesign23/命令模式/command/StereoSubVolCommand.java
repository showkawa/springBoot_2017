package com.kawa.javaDesign23.命令模式.command;

import com.kawa.javaDesign23.命令模式.command.device.Stereo;

public class StereoSubVolCommand implements Command {

    private Stereo stereo;

    public StereoSubVolCommand(Stereo stereo) {
        this.stereo = stereo;
    }

    @Override
    public void execute() {
        int vol = stereo.GetVol();
        if(vol >0){
                stereo.SteVol(--vol);
        }
        System.out.println("音响音量 " + stereo.GetVol());
    }

    @Override
    public void undo() {
        int vol = stereo.GetVol();
        if(vol <11){
            stereo.SteVol(++vol);
        }
        System.out.println("音响音量 " +stereo.GetVol());
    }
}
