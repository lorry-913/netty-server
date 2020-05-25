package com.midea.client;

import com.midea.handler.ClientInitHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

public class TestClient3 {
    public static String deviceid="3333";
    public static void main(String[] args) throws  Exception{
        EventLoopGroup eventLoopGroup=new NioEventLoopGroup();
//        try{
        Bootstrap bootstrap=new Bootstrap();
        bootstrap.group(eventLoopGroup).channel(NioSocketChannel.class).
                handler(new ClientInitHandler(deviceid));
        ChannelFuture channelFuture=bootstrap.connect("localhost",8888).sync();
        Channel channel = channelFuture.channel();
        System.out.println("client2 start");
//            Scanner scanner = new Scanner(System.in);
//            while(true){
//                System.out.println("请输入");
//                String s=scanner.next();
//                Msg msg=new Msg(deviceid,null,s);
//                channel.writeAndFlush(JacksonUtils.toJSon(msg));
//            }
//        }finally {
//            eventLoopGroup.shutdownGracefully();
//        }
    }
}
