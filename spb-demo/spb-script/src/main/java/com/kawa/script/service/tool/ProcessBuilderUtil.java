package com.kawa.script.service.tool;

import java.io.IOException;
import java.util.List;

public class ProcessBuilderUtil {

    public static void start(List<String> commands) {
        try {
            ProcessBuilder pb = new ProcessBuilder(commands);
            pb.inheritIO();
            System.exit(pb.start().waitFor());
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
        System.exit(-1);
    }
}
