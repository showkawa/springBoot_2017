package com.kawa.script.plugin;


import com.kawa.script.Plugin;

public interface ToolPlugin extends Plugin<String> {

    void start(String envPath);
}
