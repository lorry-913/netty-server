package com.midea.message;

import java.io.Serializable;

public class Msg{
    public String deviceid;

    public String target;

    public String content;

    public Msg(String deviceid, String target, String content) {
        this.deviceid = deviceid;
        this.target = target;
        this.content = content;
    }

    public Msg() {

    }


    public String getDeviceid() {
        return deviceid;
    }

    public void setDeviceid(String deviceid) {
        this.deviceid = deviceid;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Msg{" +
                "deviceid='" + deviceid + '\'' +
                ", target='" + target + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
