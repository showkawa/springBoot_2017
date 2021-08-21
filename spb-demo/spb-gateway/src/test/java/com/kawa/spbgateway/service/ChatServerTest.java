package com.kawa.spbgateway.service;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

@Slf4j
public class ChatServerTest {
    private Selector selector;
    private ServerSocketChannel listenChannel;

    public ChatServerTest() {
        try {
            selector = Selector.open();
            listenChannel = ServerSocketChannel.open();
            listenChannel.socket().bind(new InetSocketAddress(9999));
            listenChannel.configureBlocking(false);
            listenChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void listen() {
        while (true) {
            try {
                int event = selector.select();
                if (event > 0) {
                    Iterator<SelectionKey> sks = selector.selectedKeys().iterator();
                    while (sks.hasNext()) {
                        SelectionKey sk = sks.next();
                        if (sk.isAcceptable()) {
                            SocketChannel sc = listenChannel.accept();
                            sc.configureBlocking(false);
                            // when acceptable, register OP_READ event
                            sc.register(selector, SelectionKey.OP_READ);
                            log.info("online -> {}", sc.getRemoteAddress());
                        }
                        if (sk.isReadable()) {
                            reaData(sk);
                        }
                        // avoid repeat handle
                        sks.remove();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    private void reaData(SelectionKey sk) {
        SocketChannel channel = (SocketChannel) sk.channel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        try {
            int readLength = channel.read(buffer);
            if (readLength > 0) {
                String msg = new String(buffer.array()).trim();
                log.info("get msg from {} -> {}", channel.getRemoteAddress(), msg);
                // send msg to all client
                sendToAllClient(channel, msg);
            }
        } catch (IOException e) {
            try {
                log.info("offline -> {}", channel.getRemoteAddress());
                sk.channel();
                channel.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void sendToAllClient(SocketChannel channel, String msg) throws IOException {
        for (SelectionKey key : selector.keys()) {
            Channel targetChannel = key.channel();

            if (targetChannel instanceof SocketChannel && targetChannel != channel) {
                SocketChannel destination = (SocketChannel) targetChannel;
                ByteBuffer buffer = ByteBuffer.wrap(msg.getBytes());
                destination.write(buffer);
            }
        }
    }

    public static void main(String[] args) {
        new ChatServerTest().listen();
    }


}
