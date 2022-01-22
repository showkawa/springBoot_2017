package com.kawa.script.plugin;


import com.kawa.script.Plugin;

public interface CommandPlugin extends Plugin<String> {

    void run(String command);
}
