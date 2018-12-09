package com.kawa.javaDesign23.命令模式.command;

import com.kawa.javaDesign23.命令模式.command.device.Light;

public class LightOffCommand implements Command {

    private Light light;

    public LightOffCommand(Light light) {
        this.light = light;
    }

    @Override
    public void execute() {
        light.Off();
    }

    @Override
    public void undo() {
        light.On();
    }
}
