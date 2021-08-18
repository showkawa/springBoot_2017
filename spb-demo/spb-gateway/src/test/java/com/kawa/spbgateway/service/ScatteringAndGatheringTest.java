package com.kawa.spbgateway.service;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

/**
 *  telnet 127.0.0.1 9988
 */
@Slf4j
public class ScatteringAndGatheringTest {
    public static void main(String[] args) throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        InetSocketAddress inetSocketAddress = new InetSocketAddress(9988);
        serverSocketChannel.socket().bind(inetSocketAddress);

        ByteBuffer[] byteBuffers = new ByteBuffer[2];
        byteBuffers[0] = ByteBuffer.allocate(10);
        byteBuffers[1] = ByteBuffer.allocate(10);

        SocketChannel sc = serverSocketChannel.accept();

        int msgLength = 20;
        while (true) {
            int byteRead = 0;
            while (byteRead < msgLength) {
                long readL = sc.read(byteBuffers);
                byteRead += readL;
                log.info("=== byteRead ===:{}", byteRead);
                Arrays.stream(byteBuffers).forEach(bu -> {
                    log.info("=== byteRead ===:{ position:{},limit:{} }", bu.position(), bu.limit());
                });
            }

            Arrays.stream(byteBuffers).forEach(buffer -> buffer.flip());

            long byteWrite = 0;
            while (byteWrite < msgLength) {
                long writeL = sc.write(byteBuffers);
                byteWrite += writeL;
            }
            StringBuffer msg = new StringBuffer();
            Arrays.stream(byteBuffers).forEach(bu -> {
                msg.append(new String(bu.array()));
            });
            log.info("=== byteWrite current msg ===: {}", msg);
            Arrays.stream(byteBuffers).forEach(bu -> bu.clear());
            log.info(">>>>>>>>>> byteRead:{}, byteWrite:{},msgLength:{}", byteRead, byteWrite, msgLength);
        }
    }
}
