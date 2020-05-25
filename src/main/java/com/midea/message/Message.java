package com.midea.message;

import com.midea.util.Md5Util;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Message {
    public Message(MessageHead head,byte[] content) {
        this.head=head;
        this.content=content;
    }
    // 协议头
    private MessageHead head;

    // 内容
    private byte[] content;

    public MessageHead getHead() {
        return head;
    }

    public void setHead(MessageHead head) {
        this.head = head;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }
    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return "[head:"+head.toString()+"]"+"content:"+new String(content);
    }

    /**
     * 生成token   协议开始标志 +包长度+令牌生成时间+包内容+服务器与客户端约定的秘钥
     * @return
     */
    public String buidToken() {
        //生成token
        SimpleDateFormat format0 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = format0.format(this.getHead().getCreateDate());// 这个就是把时间戳经过处理得到期望格式的时间
        String allData=String.valueOf(this.getHead().getHeadData());
        allData+=String.valueOf(this.getHead().getLength());
        allData+=time;
        allData+=new String(this.getContent());
        allData+="11111";//秘钥
        return Md5Util.MD5(allData);

    }
    /**
     * 验证是否认证通过
     * @param token
     * @return
     */
    public boolean authorization(String token) {
        //表示参数被修改
        if(!token.equals(this.getHead().getToken())) {
            return false;
        }
        //验证是否失效
        Long s = (System.currentTimeMillis() - getHead().getCreateDate().getTime()) / (1000 * 60);
        if(s>60) {
            return false;
        }
        return true;
    }
}
