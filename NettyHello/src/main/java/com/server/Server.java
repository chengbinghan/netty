package com.server;


import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.handler.codec.string.StringDecoder;
import org.jboss.netty.handler.codec.string.StringEncoder;

import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author
 */
public class Server {


    public void startUp(Integer port) {

        //服务类
        ServerBootstrap bootstrap;
        bootstrap = new ServerBootstrap();

        //boss线程监听端口，worker线程负责数据读写，二者都是一个线程分配一个selectord
        ExecutorService boss = Executors.newCachedThreadPool();
        ExecutorService worker = Executors.newCachedThreadPool();

        //设置nio socket工厂
        bootstrap.setFactory(new NioServerSocketChannelFactory(boss, worker));

        //设置管道的工厂
        bootstrap.setPipelineFactory(new ChannelPipelineFactory() {

            @Override
            public ChannelPipeline getPipeline() throws Exception {
                //获取一个管道
                ChannelPipeline pipeline = Channels.pipeline();
                /*
                在管道中安装一大堆过滤器
                 */
                //handler的messageReceived方法中可以从客户端 读的数据之间强转为String
                pipeline.addLast("decoder", new StringDecoder());
                //handler的messageReceived write 数据
                pipeline.addLast("encoder", new StringEncoder());

                //添加一个handler，注意上述指定编码的代码要放到指定handler之前
                pipeline.addLast("helloHandler", new AgentChannelHandler());
                return pipeline;
            }
        });

        //服务类绑定端口
        bootstrap.bind(new InetSocketAddress(port));

        System.out.println("server start");
    }

    public static void main(String[] args) {

        new Server().startUp(10102);
    }

}
