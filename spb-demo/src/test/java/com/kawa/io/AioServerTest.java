package com.kawa.io;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

@Slf4j
public class AioServerTest {

    public static void main(String[] args) throws IOException, InterruptedException {
        // init the server socket channel and listen port 10000
        AsynchronousServerSocketChannel serverSocketChannel = AsynchronousServerSocketChannel.open().bind(new InetSocketAddress(10000));

        serverSocketChannel.accept(null, new CompletionHandler<AsynchronousSocketChannel, Void>() {
            @SneakyThrows
            @Override
            public void completed(AsynchronousSocketChannel client, Void attachment) {
                serverSocketChannel.accept(null, this);
                log.info(">>>>> connect from :{}", client.getRemoteAddress().toString());
                ByteBuffer buffer = ByteBuffer.allocate(1024);
                client.read(buffer, buffer, new CompletionHandler<>() {
                    @SneakyThrows
                    @Override
                    public void completed(Integer index, ByteBuffer buffer) {
                        buffer.flip();
                        log.info(">>>>> get message: {}", new String(buffer.array()).trim());
                        client.close();
                    }

                    @Override
                    public void failed(Throwable exc, ByteBuffer attachment) {
                        log.info(">>>>> get message exception: {}", exc.getMessage());
                    }
                });
            }

            @Override
            public void failed(Throwable exc, Void attachment) {
                log.info(">>>>> accept channel exception: {}", exc.getMessage());
            }
        });


        // to keep the process not stop
        Thread.currentThread().join();
    }
}
