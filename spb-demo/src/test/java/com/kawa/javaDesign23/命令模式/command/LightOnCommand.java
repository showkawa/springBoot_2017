package com.kawa.javaDesign23.命令模式.command;

import com.kawa.javaDesign23.命令模式.command.device.Light;

public class LightOnCommand implements Command {

    private Light light;

    public LightOnCommand(Light light) {
        this.light = light;
    }

    @Override
    public void execute() {
        light.On();
    }

    @Override
    public void undo() {
        light.Off();
    }
}
