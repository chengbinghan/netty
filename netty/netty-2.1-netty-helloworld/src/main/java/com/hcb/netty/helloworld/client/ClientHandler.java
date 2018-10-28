package com.hcb.netty.helloworld.client;

import org.jboss.netty.channel.*;

/**
 * @author ChengBing Han
 * @date 21:00  2018/10/25
 * @description
 */
public class ClientHandler extends SimpleChannelHandler {


    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {

        //1、接收数据
        /*
        没有StringDecoder时，显示字符串消息
        final ChannelBuffer message = (ChannelBuffer) e.getMessage();
        final byte[] array = message.array();
        final String msg = new String(array);
        System.out.println("message is :" + msg);
        */

        //有StringDecoder
        System.out.println(e.getMessage());

        //2、返回数据
        //返回数据给客户端
        /*
        没有StringEnCoder
        final ChannelBuffer channelBuffer = ChannelBuffers.copiedBuffer("hi, i'm server".getBytes());
        ctx.getChannel().write(channelBuffer);
        */
        //有StringEnCoder
        ctx.getChannel().write("hi, i'm client");

        //3、抛出异常，看exceptionCaught 方法
        //System.out.println(1/0);

        super.messageReceived(ctx, e);
    }

    /**
     * 在messageReceived 抛异常会调用
     * @param ctx
     * @param e
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
        System.out.println("exceptionCaught.....");
        super.exceptionCaught(ctx, e);
    }

    @Override
    public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        System.out.println("channelConnected.....");
        super.channelConnected(ctx, e);
    }


    /**
     *连接建立了关闭，会触发
     * @param ctx
     * @param e
     * @throws Exception
     */

    @Override
    public void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        System.out.println("channelDisconnected.....");

        super.channelDisconnected(ctx, e);
    }

    /**
     * channel 关闭了会触发
     * @param ctx
     * @param e
     * @throws Exception
     */
    @Override
    public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        System.out.println("channelClosed.....");

        super.channelClosed(ctx, e);
    }





    @Override
    public void writeComplete(ChannelHandlerContext ctx, WriteCompletionEvent e) throws Exception {
        System.out.println("writeComplete.....");

        super.writeComplete(ctx, e);
    }
}
