package com.kawa.io.netty.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;

@Slf4j
public class BrianHttpServerHandler extends SimpleChannelInboundHandler<HttpObject> {

    // read the client send data
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
        // check the msg is HttpRequest
        if (msg instanceof HttpRequest) {
            HttpRequest request = (HttpRequest) msg;
            URI uri = new URI(request.uri());
            if ("/kawa".equals(uri.getPath())) {
                log.info(">>>>>>>>>> client: {}", ctx.channel().remoteAddress());
                log.info(">>>>>>>>>> type: {}", msg.getClass().getName());
                log.info(">>>>>>>>>> pipeline: {}", ctx.pipeline().getClass().getName() + ctx.pipeline().hashCode());
                log.info(">>>>>>>>>> handler: {}", getClass().getName() + this.hashCode());
                sendMsg(ctx, "save the request to storage");
            } else if ("/favicon.ico".equals(uri.getPath())) {
                sendMsg(ctx, "load the favicon.ico");
            } else {
                log.info(">>>>>>>>>> client: {} type: {}", ctx.channel().remoteAddress(), msg.getClass());
                sendMsg(ctx, "404 not found");
            }
        }
    }

    private void sendMsg(ChannelHandlerContext ctx, String msg) {
        // send the http protocol response
        String template = "{\"message\":\"%s\"}";
        ByteBuf content = Unpooled.copiedBuffer(String.format(template, msg), CharsetUtil.UTF_8);
        // create a HttpResponse
        FullHttpResponse httpResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, content);
        httpResponse.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/json");
        httpResponse.headers().set(HttpHeaderNames.CONTENT_LENGTH, content.readableBytes());
        // send
        ctx.writeAndFlush(httpResponse);
    }
}
