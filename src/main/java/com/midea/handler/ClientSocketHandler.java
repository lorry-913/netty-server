package com.midea.handler;

import com.midea.message.Msg;
import com.midea.util.JacksonUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ClientSocketHandler extends SimpleChannelInboundHandler<String> {
    public String deviceid;

    public ClientSocketHandler(String deviceid){
        this.deviceid=deviceid;
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Msg msg=new Msg(deviceid,"","初始连接");
        ctx.writeAndFlush(JacksonUtils.toJSon(msg));
        super.channelActive(ctx);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String s) throws Exception {
        System.out.println("客户端接受数据:"+s);
    }
}
