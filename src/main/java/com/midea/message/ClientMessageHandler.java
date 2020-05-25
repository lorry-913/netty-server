package com.midea.message;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

import java.util.Date;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class ClientMessageHandler extends ServerMessageHandler {

    // 创建一个线程，模拟用户发送消息
    private ExecutorService executor = Executors.newSingleThreadExecutor();

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // 对于客户端，在建立连接之后，在一个独立线程中模拟用户发送数据给服务端
        executor.execute(new MessageSender(ctx));
    }

    private static final class MessageSender implements Runnable {

        private volatile ChannelHandlerContext ctx;

        public MessageSender(ChannelHandlerContext ctx) {
            this.ctx = ctx;
        }

        @Override
        public void run() {
            try {
                while (true) {
                // 模拟随机发送消息的过程
                String content = "哈哈哈哈！";
                byte[] bts = content.getBytes();
                MessageHead head = new MessageHead();
                // 令牌生成时间
                head.setCreateDate(new Date());
                head.setLength(bts.length);
                Message message = new Message(head, bts);
                message.getHead().setToken(message.buidToken());
                message.getHead().setToken(message.buidToken());
                ctx.writeAndFlush(message);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
