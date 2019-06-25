package com.hcb.netty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

/**
 * @author ice
 * @date 22:26  2019-04-02
 * @description
 */
public class Client {

    private final int port;
    private final String host;

    public Client(int port, String host) {
        this.port = port;
        this.host = host;

    }

    public void start() throws InterruptedException {
        NioEventLoopGroup nioEventLoopGroup = new NioEventLoopGroup();
        try {

            final Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(nioEventLoopGroup)
                    .channel(NioSocketChannel.class)
                    .remoteAddress(new InetSocketAddress(host, port))
                    .handler(new ChannelInitializer<SocketChannel>() {
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new ClientHandler());
                        }
                    });
            ChannelFuture channelFuture = bootstrap.connect().sync();
            channelFuture
                    .channel()
                    .closeFuture()
                    .sync();

        } finally {
            nioEventLoopGroup.shutdownGracefully().sync();
        }


    }

    public static void main(String[] args) {

        try {
            new Client(1010,"127.0.0.1").start();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
