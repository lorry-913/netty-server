package com.midea.handler;

import com.alibaba.fastjson.JSONObject;
import com.midea.channel.ChannelGroups;
import com.midea.message.Msg;
import com.midea.server.TestServer;
import com.midea.util.JacksonUtils;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;


import javax.servlet.ServletOutputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ServerSocketHandler extends SimpleChannelInboundHandler<String> {


    //异常捕获
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
//        System.out.println(cause.getMessage());
        System.out.println(ctx.channel().id()+"断开连接，移除通道");
        ChannelGroups.remove(ctx.channel());
        ctx.channel().close();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String str) throws Exception {
        System.out.println("远程地址:"+ctx.channel().remoteAddress());
        System.out.println("开始处理....");
        System.out.println(str);
        if(JacksonUtils.isJson(str)){
            Msg msg = JSONObject.parseObject(str, Msg.class);
            String deviceid=msg.getDeviceid();
            String target=msg.getTarget();
            String content=msg.getContent();
            if(!TestServer.bindMap.containsKey(deviceid)){
                TestServer.bindMap.put(deviceid,ctx.channel());
            }
            if(!target.equals("")){
                if(target.equals("all")){
                    sendMsgAll("广播"+deviceid);
                }else{
                    Channel channel=TestServer.bindMap.get(target);
                    if(channel==null){
                        System.out.println("该设备存在或者不在线...");
                        ctx.channel().writeAndFlush("该设备存在或者不在线...");
                    }else{
                        System.out.println(channel.id());
                        if(ChannelGroups.contains(channel)){
                            System.out.println("通道组包含此通道");
                            channel.writeAndFlush(JacksonUtils.toJSon(msg));
                        }
                    }
                }
            }
        }

    }

    public void sendMsgAll(String msg){
        ChannelGroups.broadcast(msg);
    }


}
