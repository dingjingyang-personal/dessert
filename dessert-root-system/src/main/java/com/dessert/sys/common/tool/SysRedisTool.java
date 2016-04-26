package com.dessert.sys.common.tool;

import com.dessert.sys.common.constants.SysSettings;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;


public class SysRedisTool {
	public static void initRedis(RedisTemplate<String, Serializable> redisTemplate){
		SysRedisTool.redisTemplate=redisTemplate;
	}
	private static RedisTemplate<String,Serializable> redisTemplate;
	@SuppressWarnings("unchecked")
	public static Map<String, Object> getCacheMap(String key) {
		Object object= redisTemplate.opsForHash().get(key, "market");
		if(object!=null){
			return (Map<String, Object>) object;
		}
		return null;
	}
	public static void expire(String key,int seconds) {
		redisTemplate.expire(key, seconds, TimeUnit.SECONDS);
	}
	public static Object getObject(String key) {
		return redisTemplate.opsForValue().get(key);
	}
	public static void setObject(String key,Serializable object) {
		redisTemplate.opsForValue().set(key, object);
	}
	public static void delete(String key) {
		redisTemplate.delete(key);
	}
	
	@SuppressWarnings("unchecked")
	public static List<Map<String,Object>> getCacheList(String key) {
		Object object= redisTemplate.opsForHash().get(key, "market");
		if(object!=null){
			return (List<Map<String, Object>>) object;
		}
		return null;
	}
	
	 public static void setCacheData(String key,Serializable data) {
		 //redisTemplate.opsForValue().set(key, data);
		  redisTemplate.opsForValue().set(key, data, SysSettings.CACHE_TIMEOUT_SECONDS, TimeUnit.SECONDS);//.expire(key,  SysSettings.CACHE_TIMEOUT_SECONDS, TimeUnit.SECONDS);
	}
	
    public static void setCacheMap(String key,Map<String, Object> data) {
    	redisTemplate.opsForHash().put(key, "market", data);
	}
    
    public static void setCacheList(String key,List<Map<String,Object>> data) {
    	redisTemplate.opsForHash().put(key, "market", data);
	}
    
    
}
