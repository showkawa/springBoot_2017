package com.kawa.io;

import org.junit.Test;

import java.nio.IntBuffer;

public class IntBufferTest {

    @Test
    public void Test_IntBuffer() {
        var allocate = IntBuffer.allocate(10);

        int i = 0;
        while (i < allocate.capacity()) {
            // put the data to buffer
            allocate.put(i++);
        }
        //
        allocate.flip();
        while (allocate.hasRemaining()) {
            int result = allocate.get();
            System.out.println("=== result ===: " + result);
        }
    }
}
