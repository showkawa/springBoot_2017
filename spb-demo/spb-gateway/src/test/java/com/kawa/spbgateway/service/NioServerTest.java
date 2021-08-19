package com.kawa.spbgateway.service;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;


@Slf4j
public class NioServerTest {

    public static void main(String[] args) throws IOException {
        // 1. create ServerSocketChannel
        ServerSocketChannel ssc = ServerSocketChannel.open();
        // 2. create Selector
        Selector selector = Selector.open();
        // 3. ServerSocketChannel  bind and listen port 9998
        ssc.socket().bind(new InetSocketAddress(9998));
        // 4. set the mode non-blocking
        ssc.configureBlocking(false);
        // 5. register the ServerSocketChannel to Selector with event "OP_ACCEPT"
        ssc.register(selector, SelectionKey.OP_ACCEPT);

        // 6. waiting the client SocketChannel connect
        while (true) {
            // get the SelectionKey and the related event
            selector.select();
            Iterator<SelectionKey> sks = selector.selectedKeys().iterator();
            while (sks.hasNext()) {
                // get the SelectionKey
                SelectionKey sk = sks.next();
                // remove the SelectionKey avoid repeat handle the key
                sks.remove();
                // handle OP_ACCEPT event
                if (sk.isAcceptable()) {
                    try {
                        SocketChannel socketChannel = ssc.accept();
                        log.info(">>>>>>>>>> connected client:{}", socketChannel.getRemoteAddress());
                        socketChannel.configureBlocking(false);
                        // register a OP_READ event to current channel
                        socketChannel.register(selector, SelectionKey.OP_READ);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                // handle OP_READ event
                if (sk.isReadable()) {
                    ByteBuffer buffer = ByteBuffer.allocate(1024);
                    SocketChannel channel = (SocketChannel) sk.channel();
                    try {
                        channel.read(buffer);
                        String msg = new String(buffer.array()).trim();
                        log.info("===== get msg from {} >> {}", channel.getRemoteAddress(), msg);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
        }
    }
}
