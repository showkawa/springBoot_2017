package com.kawa.spbgateway.service;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;

@Slf4j
public class ChatClientTest {
    private Selector selector;
    private SocketChannel socketChannel;
    private String clientName;

    public ChatClientTest() {
        try {
            selector = Selector.open();
            socketChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 9999));
            socketChannel.configureBlocking(false);
            socketChannel.register(selector, SelectionKey.OP_READ);
            clientName = socketChannel.getLocalAddress().toString().substring(1);
            log.info("{} is ready", clientName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendMsg(String msg) {
        try {
            socketChannel.write(ByteBuffer.wrap(msg.getBytes()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getMsg() {
        try {
            int select = selector.select();
            if (select > 0) {
                Iterator<SelectionKey> sks = selector.selectedKeys().iterator();
                while (sks.hasNext()) {
                    SelectionKey key = sks.next();
                    if (key.isReadable()) {
                        SocketChannel channel = (SocketChannel) key.channel();
                        ByteBuffer buffer = ByteBuffer.allocate(1024);
                        channel.read(buffer);
                        String msg = new String(buffer.array()).trim();
                        log.info("get mag from {}", msg);
                    }
                    sks.remove();
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ChatClientTest chatClientTest = new ChatClientTest();
        new Thread(() -> {
            while (true) {
                chatClientTest.getMsg();
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        // Send msg
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String msg = scanner.nextLine().trim();
            chatClientTest.sendMsg(msg);

        }

    }
}
