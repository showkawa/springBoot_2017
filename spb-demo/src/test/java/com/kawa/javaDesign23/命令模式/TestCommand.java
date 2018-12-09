package com.kawa.javaDesign23.命令模式;

import com.kawa.javaDesign23.命令模式.command.*;
import com.kawa.javaDesign23.命令模式.command.device.Light;
import com.kawa.javaDesign23.命令模式.command.device.Stereo;
import org.junit.Test;

public class TestCommand {
    @Test
    public void test(){
        CommandModeControl control = new CommandModeControl();
        MarcoCommand onCommands,offCommands;
        Light kt = new Light("客厅灯 ");
        Light cf = new Light("厨房灯 ");
        Stereo st = new Stereo();


        LightOnCommand ktLightOn =  new LightOnCommand(kt);
        LightOffCommand ktLightOff =  new LightOffCommand(kt);
        LightOnCommand cfLightOn =  new LightOnCommand(cf);
        LightOffCommand cfLightOff =  new LightOffCommand(cf);

        StereoOnCommand stereoOn = new StereoOnCommand(st);
        StereoOffCommand stereoOff = new StereoOffCommand(st);
        StereoAddVolCommand stereoAdd = new StereoAddVolCommand(st);
        StereoSubVolCommand stereoSub = new StereoSubVolCommand(st);


        Command[]  ons = {ktLightOn,cfLightOn};
        Command[]  offs = {ktLightOff,cfLightOff};
        onCommands = new MarcoCommand(ons);
        offCommands = new MarcoCommand(offs);

        control.setCommand(0,ktLightOn,ktLightOff);
        control.setCommand(1,cfLightOn,cfLightOff);
        control.setCommand(2,stereoOn,stereoOff);
        control.setCommand(3,stereoAdd,stereoSub);
        control.setCommand(4,onCommands,offCommands);

        control.onButton(0);
        control.offButton(0);
        control.onButton(1);
        control.offButton(1);
        control.onButton(2);
        control.onButton(3);
        control.offButton(3);
        control.undoButton();
        control.undoButton();
        control.undoButton();
        control.undoButton();
        control.offButton(2);
        control.onButton(4);
        control.undoButton();








    }

}
