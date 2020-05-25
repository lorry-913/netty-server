package com.midea.client;

import com.midea.handler.ClientInitHandler;
import com.midea.message.Msg;
import com.midea.util.JacksonUtils;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.Scanner;

public class TestClient {
    public static String deviceid="13355514013";

    public static void main(String[] args) throws  Exception{
        EventLoopGroup eventLoopGroup=new NioEventLoopGroup();
        try{
            Bootstrap bootstrap=new Bootstrap();
            bootstrap.group(eventLoopGroup).channel(NioSocketChannel.class).
                    handler(new ClientInitHandler(deviceid));
            ChannelFuture channelFuture=bootstrap.connect("localhost",8888).sync();
            Channel channel = channelFuture.channel();
            System.out.println("client1 start");
            Scanner scanner = new Scanner(System.in);
            while(true){
                System.out.println("请输入内容");
                String content=scanner.next();
                Msg msg=new Msg(deviceid,"2222",content);
                channel.writeAndFlush(JacksonUtils.toJSon(msg));
            }
        }finally {
            eventLoopGroup.shutdownGracefully();
        }
    }
}
