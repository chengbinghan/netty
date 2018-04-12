package com.client;


import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.handler.codec.string.StringDecoder;
import org.jboss.netty.handler.codec.string.StringEncoder;

import java.net.InetSocketAddress;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Client {

    static ChannelFuture connect;

    //客户端，服务类
    static ClientBootstrap bootstrap;

    public static void start() {

        bootstrap = new ClientBootstrap();

        //线程池
        ExecutorService boss = Executors.newCachedThreadPool();
        ExecutorService worker = Executors.newCachedThreadPool();

        //socket工厂
        bootstrap.setFactory(new NioClientSocketChannelFactory(boss, worker));

        //管道工厂
        bootstrap.setPipelineFactory(new ChannelPipelineFactory() {

            @Override
            public ChannelPipeline getPipeline() throws Exception {
                ChannelPipeline pipeline = Channels.pipeline();
                pipeline.addLast("decoder", new StringDecoder());
                pipeline.addLast("encoder", new StringEncoder());
                pipeline.addLast("hiHandler", new HiHandler());
                return pipeline;
            }
        });

        //连接服务端
        connect = bootstrap.connect(new InetSocketAddress("127.0.0.1", 10102));
        Channel channel = connect.getChannel();

        System.out.println("client start");

        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("请输入");
            channel.write(scanner.next());
        }
    }

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            start();
            boolean success = connect.isSuccess();
            if (success) {
                break;
            }
        }
        boolean success = Client.connect.isSuccess();
        System.out.println(success);
    }

}
