package com.midea.server;

import com.midea.handler.ServerInitHandler;
import com.midea.util.JedisUtil;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class TestServer {
    public static ConcurrentMap<String, Channel> bindMap=new ConcurrentHashMap<String, Channel>();

    public static void main(String[] args) throws Exception{
        //建立两个线程组，可以看做是死循环 一直处理请求
        EventLoopGroup bossGroup=new NioEventLoopGroup();//这个线程组对客户端不断接受链接 但是不处理 由work线程组处理
        EventLoopGroup workGroup=new NioEventLoopGroup();
        try{
            JedisUtil.init();
            ServerBootstrap sb=new ServerBootstrap();//启动服务端的类
            //使用NioServerSocketChannel这个管道，添加处理器
            sb.group(bossGroup,workGroup).channel(NioServerSocketChannel.class).
                    handler(new LoggingHandler(LogLevel.INFO)).//boss线程处理
                    childHandler(new ServerInitHandler());//work线程处理
            ChannelFuture channelFuture=sb.bind(8888).sync();
            channelFuture.channel().closeFuture().sync();//sync表示一直等待 否则会退出
        }finally {
            //优雅关闭
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }


    }
}
