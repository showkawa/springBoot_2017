package com.kawa.javaDesign23.命令模式.command;
//宏命令  批量执行
public class MarcoCommand implements Command {

    private Command[] commands;

    public MarcoCommand(Command[] commands) {
        this.commands = commands;
    }

    @Override
    public void execute() {
        for (int i = 0; i <commands.length ; i++) {
            commands[i].execute();
        }
    }

    @Override
    public void undo() {
        for (int i = 0; i <commands.length ; i++) {
            commands[i].undo();
        }
    }
}
