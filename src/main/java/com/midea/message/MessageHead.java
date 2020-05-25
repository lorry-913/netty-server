package com.midea.message;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MessageHead {
    private int headData=0X76;//协议开始标志
    private int length;//包的长度
    private String token;
    private Date createDate;

    public int getHeadData() {
        return headData;
    }
    public void setHeadData(int headData) {
        this.headData = headData;
    }
    public int getLength() {
        return length;
    }
    public void setLength(int length) {
        this.length = length;
    }


    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
    public Date getCreateDate() {
        return createDate;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    @Override
    public String toString() {
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // TODO Auto-generated method stub
        return "headData："+headData+",length:"+length+",token:"+token+",createDate："+    simpleDateFormat.format(createDate);
    }
}
