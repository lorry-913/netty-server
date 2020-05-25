package com.midea.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;

public class ClientInitHandler extends ChannelInitializer<SocketChannel> {
    public String deviceid;

    public ClientInitHandler(){

    }

    public ClientInitHandler(String deviceid){
        this.deviceid=deviceid;
    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        super.channelRead(ctx, msg);
    }

    //连接一旦创建就会执行此方法，初始化管道
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
//        System.out.println("管道初始化成功...");
        //通过SocketChannel获得管道流水线
        ChannelPipeline pipeline =socketChannel.pipeline();

        pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE,0,4,0,4));//Http的编码
        pipeline.addLast(new LengthFieldPrepender(4));

        pipeline.addLast(new StringDecoder(CharsetUtil.UTF_8));
        pipeline.addLast(new StringEncoder(CharsetUtil.UTF_8));
        pipeline.addLast(new ClientPingerHandler());
        pipeline.addLast(new ClientSocketHandler(deviceid));

    }
}
