package com.kawa.io.netty.simple;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class BrianClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info(">>>>>>>>>> BrianClientHandler channelActive ");
        sendMsg(ctx, "BrianClientHandler channelActive");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        log.info(">>>>>>>>>> server: {}{}", ctx.channel().remoteAddress(), getMsg(buf));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error(">>>>>>>>>> BrianClientHandler error: {}", cause.getMessage());
        ctx.close();
    }

    private void sendMsg(ChannelHandlerContext ctx, String message){
        ctx.writeAndFlush(Unpooled.copiedBuffer(message, CharsetUtil.UTF_8));
    }

    private String getMsg(ByteBuf buf){
        return buf.toString(CharsetUtil.UTF_8);
    }
}