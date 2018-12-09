package com.kawa.javaDesign23.命令模式.command;

import com.kawa.javaDesign23.命令模式.command.device.Stereo;

public class StereoOffCommand implements Command {

    private Stereo stereo;

    public StereoOffCommand(Stereo stereo) {
        this.stereo = stereo;
    }

    @Override
    public void execute() {
        stereo.Off();
    }

    @Override
    public void undo() {
        stereo.On();
        stereo.SetMusic();
    }
}
