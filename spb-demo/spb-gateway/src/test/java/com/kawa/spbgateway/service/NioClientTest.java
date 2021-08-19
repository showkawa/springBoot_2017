package com.kawa.spbgateway.service;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.UUID;

@Slf4j
public class NioClientTest {
    public static void main(String[] args) throws IOException, InterruptedException {
        //1. get a SocketChannel
        SocketChannel sc = SocketChannel.open();
        //2. set non-blocking mode
        sc.configureBlocking(false);
        //3. set connect server address and port
        InetSocketAddress inetSocketAddress = new InetSocketAddress("127.0.0.1", 9998);
        //4. connect server
        if (!sc.connect(inetSocketAddress)) {
            while (!sc.finishConnect()) {
                log.info(">>>>>>>> connecting to server");
            }
        }
        //5. send msg
        while (true) {
            log.info(">>>>>>>> send mag to server");
            String msg = UUID.randomUUID().toString();
            ByteBuffer buffer = ByteBuffer.wrap(msg.getBytes());
            sc.write(buffer);
            Thread.sleep(5000);
        }


    }
}
