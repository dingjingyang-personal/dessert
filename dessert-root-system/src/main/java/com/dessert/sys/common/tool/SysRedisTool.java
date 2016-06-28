package com.dessert.sys.common.tool;

import com.dessert.sys.common.constants.SysSettings;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;


public class SysRedisTool {

    private static RedisTemplate<String, Serializable> redisTemplate;

    public static void initRedis(RedisTemplate<String, Serializable> redisTemplate) {
        SysRedisTool.redisTemplate = redisTemplate;
    }


    /**
     * 存储数据
     * @param key
     * @param data
     */
    public static void setCacheData(String key, Serializable data) {
        redisTemplate.opsForValue().set(key, data, SysSettings.CACHE_TIMEOUT_SECONDS, TimeUnit.SECONDS);
    }


    /**
     * 获取数据
     * @param key
     * @return
     */
    public static Object getObject(String key) {
        return redisTemplate.opsForValue().get(key);
    }


    /**
     * 删除数据
     * @param key
     */
    public static void delete(String key) {
        redisTemplate.delete(key);
    }


}
