package com.hcb.netty.helloworld.client;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.*;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.handler.codec.string.StringDecoder;
import org.jboss.netty.handler.codec.string.StringEncoder;

import java.net.InetSocketAddress;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author ChengBing Han
 * @date 15:35  2018/10/28
 * @description
 */
public class Client {
    public static void main(String[] args) {
        //1、服务类
        final ClientBootstrap bootstrap = new ClientBootstrap();

        //2、线程池
        final ExecutorService boss = Executors.newCachedThreadPool();
        final ExecutorService worker = Executors.newCachedThreadPool();

        //3、socket 工厂
        bootstrap.setFactory(new NioClientSocketChannelFactory(boss, worker));
        //4、管道工厂
        bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
            public ChannelPipeline getPipeline() throws Exception {
                final ChannelPipeline pipeline = Channels.pipeline();
                pipeline.addLast("encoder",new StringEncoder());
                pipeline.addLast("decoder",new StringDecoder());
                pipeline.addLast("ClientHandler", new ClientHandler());

                return pipeline;
            }
        });

        //5、连接服务端
        final ChannelFuture channelFuture = bootstrap.connect(new InetSocketAddress("127.0.0.1", 10101));

        //6、输入数据
        final Channel channel = channelFuture.getChannel();
        final Scanner scanner = new Scanner(System.in);
        while (true){
            System.out.println("请输入：");
            final String msg = scanner.next();
            channel.write(msg);
        }
    }
}
