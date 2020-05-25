package com.midea.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.CharsetUtil;

import java.util.concurrent.TimeUnit;

//初始化过滤器，这个可以自己添加各种过滤器
public class ServerInitHandler extends ChannelInitializer<SocketChannel> {
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

        //三个空闲时间参数，以及时间参数的格式。我们的例子中设置的是2,2,2，意思就是客户端2秒没有读/写，这个超时时间就会被触发。超时事件触发就需要我们来处理了，
        pipeline.addLast(new IdleStateHandler(0,2,0, TimeUnit.SECONDS));

//        pipeline.addLast(new HttpServerCodec());//Http的编码
//        pipeline.addLast("ServerHttpHandler",new ServerHttpHandler());
        pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE,0,4,0,4));//Http的编码
        pipeline.addLast(new LengthFieldPrepender(4));
        pipeline.addLast(new StringDecoder(CharsetUtil.UTF_8));
        pipeline.addLast(new StringEncoder(CharsetUtil.UTF_8));
        pipeline.addLast(new ServerPongerHandler());
        pipeline.addLast(new ServerSocketHandler());

    }
}
