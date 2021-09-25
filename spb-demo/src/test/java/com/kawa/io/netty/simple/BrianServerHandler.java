package com.kawa.io.netty.simple;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Slf4j
public class BrianServerHandler extends ChannelInboundHandlerAdapter {
    private ConcurrentHashMap<String, String> storage = new ConcurrentHashMap<>();

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info(">>>>>>>>>> channelActive current thread: {}", Thread.currentThread().getName());
        ctx.channel().eventLoop().execute(()-> {
            // save to storage
            storage.put(ctx.channel().remoteAddress().toString(), "Y");
        });
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Channel channel = ctx.channel();
        // convert the msg to ByteBuf
        ByteBuf buf = (ByteBuf) msg;
        log.info(">>>>>>>>>> get msg from client:{}, msg:{}", channel.remoteAddress(), getMsg(buf));
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        sendMsg(ctx," get the msg");
    }

    // when hit the Exception can close the related channel
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error(">>>>>>>>>> BrianServerHandler error: {}", cause.getMessage());
        ctx.channel().eventLoop().schedule(()-> {
            // update the storage
            storage.put(ctx.channel().remoteAddress().toString(), "N");
        }, 20L, TimeUnit.MILLISECONDS);
        ctx.close();
    }

    private void sendMsg(ChannelHandlerContext ctx, String message){
        ctx.writeAndFlush(Unpooled.copiedBuffer(message, CharsetUtil.UTF_8));
    }

    private String getMsg(ByteBuf buf){
        return buf.toString(CharsetUtil.UTF_8);
    }
}
