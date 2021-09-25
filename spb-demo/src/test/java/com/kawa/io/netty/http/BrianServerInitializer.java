package com.kawa.io.netty.http;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BrianServerInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        // add handler
        ch.pipeline()
                // add Netty Encoder-Decoder
                .addLast("httpServerCodec", new HttpServerCodec())
                // add customized handler
                .addLast("brianHttpServerHandler", new BrianHttpServerHandler());
    }
}
