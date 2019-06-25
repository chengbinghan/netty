package com.hcb.netty.http.server;


import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;


/**
 * Title: ServerLancher
 */
@Slf4j
public class ServerLancher implements Runnable {
    private static final int port = Config.PORT;//设置服务端端口
    private static EventLoopGroup group = new NioEventLoopGroup();   // 通过nio方式来接收连接和处理连接
    private static ServerBootstrap bootstrap = new ServerBootstrap();

    private static volatile boolean started = false;
    public static volatile boolean startCompleted = false;


    public static void main(String[] args) {
        start();
    }

    /**
     * Netty创建全部都是实现自AbstractBootstrap。
     * 客户端的是Bootstrap，服务端的则是	ServerBootstrap。
     **/
    public synchronized static void start() {
        if (!started) {
            Thread thread = new Thread(new ServerLancher());
            thread.setName("server-thread");
            thread.start();
            started = true;
        }

    }


    @Override
    public void run() {
        try {
            bootstrap.group(group);
            bootstrap.channel(NioServerSocketChannel.class);
            bootstrap.childHandler(new NettyServerFilter()); //设置过滤器
            // 服务器绑定端口监听
            ChannelFuture f = bootstrap.bind(port).sync();
            log.info("server start success,listen on port:{}", port);
            startCompleted = true;
            // 监听服务器关闭监听
            f.channel().closeFuture().sync();
        } catch (Exception e) {
            log.error("failed to start http server. {},\r\n{}", e.getCause(), e.getStackTrace());
        } finally {
            group.shutdownGracefully(); //关闭EventLoopGroup，释放掉所有资源包括创建的线程
        }
    }
}
