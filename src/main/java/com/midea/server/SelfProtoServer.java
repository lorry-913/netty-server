package com.midea.server;

import com.midea.handler.ServerInitHandler;
import com.midea.message.MessageDecoder;
import com.midea.message.MessageEncoder;
import com.midea.message.ServerMessageHandler;
import com.midea.util.JedisUtil;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class SelfProtoServer {

    public static void main(String[] args) throws Exception{
        //建立两个线程组，可以看做是死循环 一直处理请求
        EventLoopGroup bossGroup=new NioEventLoopGroup();//这个线程组对客户端不断接受链接 但是不处理 由work线程组处理
        EventLoopGroup workGroup=new NioEventLoopGroup();
        try{
            JedisUtil.init();
            ServerBootstrap sb=new ServerBootstrap();//启动服务端的类
            //使用NioServerSocketChannel这个管道，添加处理器
            sb.group(bossGroup,workGroup).channel(NioServerSocketChannel.class).
                    option(ChannelOption.SO_BACKLOG, 1024).
                    handler(new LoggingHandler(LogLevel.INFO)).//boss线程处理
                    childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ChannelPipeline pipeline = ch.pipeline();
                    // 添加用于处理粘包和拆包问题的处理器
                    pipeline.addLast(new LengthFieldBasedFrameDecoder(1024, 0, 4, 0, 4));
                    pipeline.addLast(new LengthFieldPrepender(4));
                    // 添加自定义协议消息的编码和解码处理器
                    pipeline.addLast(new MessageEncoder());
                    pipeline.addLast(new MessageDecoder());
                    // 添加具体的消息处理器
                    pipeline.addLast(new ServerMessageHandler());
                }

            });//work线程处理
            ChannelFuture channelFuture=sb.bind(8888).sync();
            channelFuture.channel().closeFuture().sync();//sync表示一直等待 否则会退出
        }finally {
            //优雅关闭
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }


    }
}
