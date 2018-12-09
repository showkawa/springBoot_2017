package com.kawa.javaDesign23.命令模式.command;

public interface Control {
    public void onButton(int slot);
    public void offButton(int slot);
    public void undoButton();

}
