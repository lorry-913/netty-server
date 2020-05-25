package com.midea.handler;

import com.alibaba.fastjson.JSON;
import com.midea.channel.ChannelGroups;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.ChannelMatcher;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @author wqy
 * @version 1.0
 * @date 2019/8/8 15:59
 */
public class ServerWebSocketHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    //创建一个时间生成器
    private SimpleDateFormat sdf=new SimpleDateFormat("yyyy-mm-dd hh:MM");

    /**
     * 通道建立调用
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

    }

    /**
     * 通道断开调用
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {

    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        //首次连接是FullHttpRequest
        System.out.println("webscoket:"+msg);
        if (null != msg && msg instanceof FullHttpRequest) {
            FullHttpRequest request = (FullHttpRequest) msg;
            String uri = request.uri();
            System.out.println(uri);
            Map paramMap=getUrlParams(uri);
            //System.out.println("接收到的参数是："+JSON.toJSONString(paramMap));
            //如果url包含参数，需要处理
            if(uri.contains("?")){
                String newUri=uri.substring(0,uri.indexOf("?"));
                System.out.println(newUri);
                request.setUri(newUri);
            }
        }else if(msg instanceof TextWebSocketFrame){
            //正常的TEXT消息类型
            TextWebSocketFrame frame=(TextWebSocketFrame)msg;
            System.out.println("客户端收到服务器数据：" +frame.text());
            System.out.println(frame.text());
//            HashMap<String,String> hashMap = JSON.parseObject(frame.text(), HashMap.class);//绑定通道和skey
//            channelMap.put(hashMap.get("skey"),ctx.channel());
//            HashMap<Integer,String> hashMap = JSON.parseObject(frame.text(), HashMap.class);
//            System.out.println(hashMap.get("clientId"));
//            Channel friendChannel = UserChannelMap.getChannelById(hashMap.get("clientId"));
//            if (friendChannel!=null){ //如果用户在线,直接发送给好友
//                friendChannel.writeAndFlush(new TextWebSocketFrame(hashMap.get("content")));
//            }else {//如果用户不在线,暂时不发送
////                System.out.println("用户"+record.getFriendid()+"不在线");
//            }

//            sendluruAllMessage(frame.text());
        }else{
            System.out.println("不是websocket转发给下个pipeline");
            ctx.fireChannelRead(msg);
        }
    }

    private void sendAllMessage(String message){
        //收到信息后，群发给所有channel
        ChannelGroups.broadcast( new TextWebSocketFrame(message));
    }

    private void sendOneMessage(String message,Channel ch){
        //收到信息后，群发给所有channel
        ChannelGroups.broadcast(new TextWebSocketFrame(message), new ChannelMatcher() {
            @Override
            public boolean matches(Channel channel) {
                if(ch.id().equals(channel.id())){
                    return true;
                }
                return false;
            }
        });
    }

    private static Map getUrlParams(String url){
        Map<String,String> map = new HashMap<>();
        url = url.replace("?",";");
        if (!url.contains(";")){
            return map;
        }
        if (url.split(";").length > 0){
            String[] arr = url.split(";")[1].split("&");
            for (String s : arr){
                String key = s.split("=")[0];
                String value = s.split("=")[1];
                map.put(key,value);
            }
            return  map;

        }else{
            return map;
        }
    }
}
