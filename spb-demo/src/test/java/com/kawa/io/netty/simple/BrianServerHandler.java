package com.kawa.io.netty.simple;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BrianServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info(">>>>>>>>>> BrianServerHandler channelActive ");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        log.info(">>>>>>>>>> current thread: {}", Thread.currentThread().getName());
        log.info(">>>>>>>>>> ChannelHandlerContext: {}", ctx);
        Channel channel = ctx.channel();
//        ChannelPipeline pipeline = ctx.pipeline();
        // convert the msg to ByteBuf
        ByteBuf buf = (ByteBuf) msg;
        log.info(">>>>>>>>>> get msg from client:{}, msg:{}", channel.remoteAddress(), buf.toString(CharsetUtil.UTF_8));
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        String msg = " get the msg";
        ctx.writeAndFlush(Unpooled.copiedBuffer(msg, CharsetUtil.UTF_8));
    }

    // when hit the Exception can close the related channel
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error(">>>>>>>>>> BrianServerHandler error: {}", cause.getMessage());
        ctx.close();
    }
}
