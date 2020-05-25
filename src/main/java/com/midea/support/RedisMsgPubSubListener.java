package com.midea.support;

import com.midea.util.CommonUtils;
import com.midea.util.StringUtil;
import org.apache.log4j.Logger;
import redis.clients.jedis.JedisPubSub;

public class RedisMsgPubSubListener extends JedisPubSub {
    private static Logger log = Logger.getLogger(RedisMsgPubSubListener.class);

    @Override
    public void onMessage(String channel, String message) {

        if(CommonUtils.isStrNull(message)){
            return;
        }

    }

    @Override
    public void onSubscribe(String channel, int subscribedChannels) {
        log.info("channel:" + channel + "is been subscribed:" + subscribedChannels);
    }

    @Override
    public void onUnsubscribe(String channel, int subscribedChannels) {
        log.info("channel:" + channel + "is been unsubscribed:" + subscribedChannels);
    }
}
