package com.server;


import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;

/**
 * 消息接受处理类,继承simpleChannelHandler
 *
 * @author -hcb-
 */
public class AgentChannelHandler extends SimpleChannelHandler {


    /**
     * 接收消息
     * 应用场景：
     * 除了在Server的管道工厂中getPipeline设置过滤器，还可以在此处添加功能，比如，防止恶意攻击，别人可能写代码，频繁地连接我们的server.
     * 此时，我们可以添加监控代码，发现一个客户端ip，在1s内发送了多条消息，那么我们就给这个ip 拉黑。
     * 拉黑处理：
     * 1、在messageReceived处，直接调用ctx.getChannel().close();关闭这个通道
     * 2、在连接处，channelConnected（）方法处不让这个ip连接
     */
    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {


        String msg = (String) e.getMessage();
        System.out.println(msg);

        ctx.getChannel().write("hi, this is server");


       // super.messageReceived(ctx, e);
    }
    /**
     * 捕获异常
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
        System.out.println("exceptionCaught");
        System.out.println(e.getCause().getMessage());
        super.exceptionCaught(ctx, e);
    }

    /**
     * 新连接
     */
    @Override
    public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        System.out.println("channelConnected");
        super.channelConnected(ctx, e);
    }

    /**
     * 必须是链接已经建立，关闭通道的时候才会触发
     * 应用场景：
     * 比如一个游戏服务器，当玩家连接时，我们要把一些缓存信息放到redis中，同样，玩家下线时，我们根据这个方法，将
     * 玩家在redis 中的缓存清空。
     */
    @Override
    public void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        System.out.println("channelDisconnected");
        super.channelDisconnected(ctx, e);
    }

    /**
     * channel关闭的时候触发
     * <p/>
     * channelDisconnected只有在连接建立后断开才会调用
     * channelClosed无论连接是否成功都会调用关闭资源
     */
    @Override
    public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        System.out.println("channelClosed");
        super.channelClosed(ctx, e);
    }


}
