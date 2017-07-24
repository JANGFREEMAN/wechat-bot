package com.wechat.redis;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 *  缓存工具类
 */
public class RedisUtils {

    private static  String IP_ADDRESS = "127.0.0.1";

    private static int PORT = 6379;

    private static JedisPool jedisPool;

    private RedisUtils(){

    }

    /**
     * 保存用户信息到缓存中
     * @param jsonArray 用户信息，数组类型
     */
    public static void saveUserArr(JSONArray jsonArray){
        Jedis jedis = getJedis();
        jedis.flushDB();
        for(int i=0 ; i < jsonArray.size() ; i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String userName = jsonObject.getString("UserName");
            if(!jedis.exists(userName)){
                jedis.set(userName,jsonObject.toJSONString());
            }
        }
    }

    /**
     * 获取jedis连接
     * @return
     */
    public static  synchronized Jedis getJedis(){
        if(jedisPool == null){
            JedisPoolConfig poolConfig = new JedisPoolConfig();
            //最大空闲连接数
            poolConfig.setMaxIdle(10);
            //连接池中最大连接数
            poolConfig.setMaxTotal(100);
            //在获取链接的时候设置的超市时间
            poolConfig.setMaxWaitMillis(1000);
            //表示在向连接池中创建连接的时候会对链接进行测试，保证连接池中的链接都是可用的。
            poolConfig.setTestOnBorrow(true);
            jedisPool = new JedisPool(poolConfig, IP_ADDRESS, PORT);
        }
        return jedisPool.getResource();
    }

    /**
     * 释放jedis连接资源
     * @param jedis
     */
    public static synchronized void  returnResource(Jedis jedis){
        jedisPool.returnResourceObject(jedis);
    }
}
