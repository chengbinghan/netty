package com.hcb.netty.server;


import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

/**
 * @author ice
 * @date 17:29  2019-04-01
 * @description
 */
public class EchoServer {

    private final int port;

    public EchoServer(int port) {
        this.port = port;
    }


    public void start() throws InterruptedException {
        NioEventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(eventLoopGroup)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(port))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) {
                            socketChannel.pipeline()
                                    .addLast(new ServerHandler());
                        }
                    });

            ChannelFuture channelFuture = bootstrap.bind().sync();
            System.out.println("server " + EchoServer.class + " start up ,"
                    + " listen on " + channelFuture.channel().localAddress());

            channelFuture.channel().closeFuture().sync();


        } finally {
            eventLoopGroup.shutdownGracefully().sync();
        }
    }


    public static void main(String[] args) {
        try {
            new EchoServer(1010).start();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
