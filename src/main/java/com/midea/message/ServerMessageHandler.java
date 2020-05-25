package com.midea.message;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerMessageHandler extends SimpleChannelInboundHandler<Message> {
    // 获取一个消息处理器工厂类实例

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message message) throws Exception {
        System.out.println(message);
    }
}
