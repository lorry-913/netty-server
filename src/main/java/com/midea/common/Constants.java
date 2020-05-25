package com.midea.common;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * @author lurui
 */
public class Constants {

    public static final Integer MAGIC_NUMBER = 0x1314;    //魔数
    public static final Byte MAIN_VERSION = 1;        // 主版本号
    public static final Byte SUB_VERSION = 1;        // 次版本号
    public static final Byte MODIFY_VERSION = 1;// 修订版本号
    public static final Byte SESSION_ID_LENGTH = 1;
    // ******redis数据配置文件********//
    public static Properties redisProperties = new Properties();

    static {
        System.out.println(Constants.class.getClassLoader().getResource("redis.properties"));
        if (null != Constants.class.getClassLoader().getResource("redis.properties")) {
            InputStream redisIn = Constants.class.getClassLoader().getResourceAsStream("redis.properties");
            try {
                BufferedReader redisBuffer = new BufferedReader(new InputStreamReader(redisIn));
                redisProperties.load(redisBuffer);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static String getRedisPropertiesValue(String str) {
        return redisProperties.getProperty(str);
    }


}
