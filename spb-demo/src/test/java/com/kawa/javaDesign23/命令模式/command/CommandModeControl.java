package com.kawa.javaDesign23.命令模式.command;

import java.util.Stack;

public class CommandModeControl implements Control {
    private Command[] onCommands;
    private Command[] offCommands;
    //栈队列  先进后出
    private Stack<Command> stack = new Stack<>();

    public CommandModeControl() {
        onCommands = new Command[5];
        offCommands = new Command[5];
        NoCommand noCommand = new NoCommand();
        for (int i = 0; i <onCommands.length ; i++) {
            onCommands[i] = noCommand;
            offCommands[i] = noCommand;
        }
    }

    public void setCommand (int slot,Command onCommand,Command offCommand){
        onCommands[slot] = onCommand;
        offCommands[slot] = offCommand;
    }


    @Override
    public void onButton(int slot) {
        onCommands[slot].execute();
        stack.push(onCommands[slot]);
    }

    @Override
    public void offButton(int slot) {
        offCommands[slot].execute();
        stack.push(offCommands[slot]);
    }

    @Override
    public void undoButton() {
        //stack.pop().undo(); //pop会把栈顶的值删除
        stack.peek().undo(); //peek 不改变栈的值
    }
}
