package com.kawa.java8.stream;

import org.junit.Test;

import java.io.IOException;

public class BrianAutoCloseable implements AutoCloseable {

    @Test
    public void Test_Closeable() throws IOException {
        try (BrianAutoCloseable bac = new BrianAutoCloseable()) {
            bac.handle();
        }
    }

    public void handle() {
        System.out.println("---- handle ----");
    }

    @Override
    public void close() throws IOException {
        System.out.println("---close---");
    }


}
