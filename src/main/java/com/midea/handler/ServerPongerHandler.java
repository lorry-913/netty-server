package com.midea.handler;

import com.midea.channel.ChannelGroups;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

public class ServerPongerHandler extends SimpleChannelInboundHandler<String> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String s) throws Exception {
        if("I am alive".equals(s)){
            ctx.channel().writeAndFlush("copy that");
        }else {
            ctx.fireChannelRead(s);//继续把数据流向下个handler
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("客户端通道激活："+ctx.channel().id());
        ChannelGroups.add(ctx.channel());
        System.err.println("=== " + ctx.channel().remoteAddress() + " is active ===");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Disconnected with the remote client.");
        // do something
        ctx.fireChannelInactive();

    }
}
