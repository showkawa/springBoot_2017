package com.kawa.io.netty.simple;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BrianServer {
    public static void main(String[] args) {
        // create the bossGroup and workerGroup
        // bossGroup only handle the connect request
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
        // workerGroup  handle the business request, thread pool size = cup * 2
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            // ServerBootstrap application start class
            ServerBootstrap bootstrap = new ServerBootstrap();
            // set the parent group and child group
            bootstrap.group(bossGroup, workerGroup)
                    // NioServerSocketChannel use as server channel
                    .channel(NioServerSocketChannel.class)
                    // set
                    .option(ChannelOption.SO_BACKLOG, 128)
                    // set keep alive connect
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    // create a SocketChannel
                    // set Handler to pipeline of workerGroup's EventLoop
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        // set Handler for pipeline
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new BrianServerHandler());
                        }
                    });
            log.info("---------- BrianServer is ready  ----------");
            // start the server bind port and sync create ChannelFuture
            ChannelFuture cf = bootstrap.bind(9001).sync();
            // listen the close channel
            cf.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }

}