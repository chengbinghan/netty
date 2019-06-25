package com.hcb.netty.http.server;


import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.util.Date;

/**
 * Title: NettyServerHandler
 * Description: 服务端业务逻辑
 * Version:1.0.0
 */
@Slf4j
public class NettyServerHandler extends ChannelInboundHandlerAdapter {
    private String result = "";
    private static final String RESPONSE_TEXT = "success";

    /*
     * 收到消息时，返回信息
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (!(msg instanceof FullHttpRequest)) {
            result = "未知请求!";
            responseJson(ctx, result, HttpResponseStatus.BAD_REQUEST);
            return;
        }
        FullHttpRequest httpRequest = (FullHttpRequest) msg;
        try {


            String path = httpRequest.getUri();            //获取路径
            String body = getBody(httpRequest);    //获取参数
            HttpMethod method = httpRequest.getMethod();//获取请求方法

            log.info("param:{}", body);

            if (Config.TEST_PATH_JSON.equalsIgnoreCase(path) && HttpMethod.POST.equals(method)) {

                handleJson(ctx, body);

            }else if(Config.TEST_PATH_TEXT.equalsIgnoreCase(path) && HttpMethod.POST.equals(method)){

                handleText(ctx,body);
            }

        } catch (Exception e) {
            log.error("failed {} ,{}", (Object[]) e.getCause().getStackTrace());

        } finally {
            //释放请求
            httpRequest.release();
        }
    }

    private void handleText(ChannelHandlerContext ctx, String body) {

        // TODO: 2019-06-25 业务逻辑

        responseText(ctx, RESPONSE_TEXT + new Date(), HttpResponseStatus.OK);
    }

    private void handleJson(ChannelHandlerContext ctx, String body) {

        // TODO: 2019-06-25 业务逻辑

        //接受到的消息，做业务逻辑处理...

       String response =  "{\"msg\":\"success\",\"date\": \""+ new Date() +"\"}";
        responseJson(ctx, response, HttpResponseStatus.OK);
    }


    /**
     * 获取body参数
     *
     * @param request
     * @return
     */
    private String getBody(FullHttpRequest request) {
        ByteBuf buf = request.content();
        return buf.toString(CharsetUtil.UTF_8);
    }

    /**
     * 发送的返回值
     *
     * @param ctx     返回
     * @param context 消息
     * @param status  状态
     */
    private void responseJson(ChannelHandlerContext ctx, String context, HttpResponseStatus status) {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status, Unpooled.copiedBuffer(context, CharsetUtil.UTF_8));
        response.headers().set("Content-Type", "application/json;charset=UTF-8");

        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);

    }

    /**
     * 发送的返回值
     *
     * @param ctx     返回
     * @param context 消息
     * @param status  状态
     */
    private void responseText(ChannelHandlerContext ctx, String context, HttpResponseStatus status) {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status, Unpooled.copiedBuffer(context, CharsetUtil.UTF_8));

        response.headers().set("Content-Type", "text/plain; charset=UTF-8");

        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);

    }


    /*
     * 建立连接时，返回消息
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //System.out.println("连接的客户端地址:" + ctx.channel().remoteAddress());
        ctx.writeAndFlush("a client connect service, client" + InetAddress.getLocalHost().getHostName() + " success connected");
        super.channelActive(ctx);
    }
}
