package com.midea.handler;

import com.midea.util.JacksonUtils;
import com.midea.util.UrlUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

//InboundHandler进来处理
public class ServerHttpHandler extends SimpleChannelInboundHandler<HttpObject> {
    public static ConcurrentMap<String,String> bindMap=new ConcurrentHashMap<String, String>();


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject httpObject) throws Exception {
        //做请求判断 否则报错

        if(httpObject instanceof HttpRequest){
            System.out.println(ctx.channel().remoteAddress());
            HttpRequest httpRequest=(HttpRequest)httpObject;
            System.out.println("请求方式"+httpRequest.method().name());
            System.out.println("请求参数"+JacksonUtils.toJSon(UrlUtil.parse(httpRequest.uri())));
            ByteBuf content=Unpooled.copiedBuffer("hello word",CharsetUtil.UTF_8);
            FullHttpResponse response = new DefaultFullHttpResponse(
                    HttpVersion.HTTP_1_1,
                    HttpResponseStatus.OK,
                    content);;//设置版本号 发送内容
            response.headers().set(HttpHeaderNames.CONTENT_TYPE,"text/plain");//设置头信息
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH,content.readableBytes());//设置头信息
            ctx.writeAndFlush(response);
//            return;
            ctx.close(); //http是无状态的 请求玩要关闭连接
        }else{

        }

    }

    //通道被注册 只执行一次
    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
    }

    //通道被激活 只执行一次
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }

    //通道被添加 只执行一次
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        super.handlerAdded(ctx);
    }
}
