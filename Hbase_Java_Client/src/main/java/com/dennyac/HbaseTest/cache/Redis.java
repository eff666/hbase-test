package com.dennyac.HbaseTest.cache;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisSentinelPool;

import java.util.ResourceBundle;

/**
 * Created by shuyun on 2016/11/1.
 */
public class Redis {
    private static JedisPool pool;
    static {
        //读取配置文件
        ResourceBundle bundle = ResourceBundle.getBundle(("redis"));

        if (bundle == null) {
            throw new IllegalArgumentException(
                    "[redis.properties] is not found!");
        }
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(Integer.valueOf(bundle.getString("redis.pool.maxTotal")));
        config.setMaxIdle(Integer.valueOf(bundle.getString("redis.pool.maxIdle")));
        config.setMaxWaitMillis(Long.valueOf(bundle.getString("redis.pool.maxWaitMillis")));
        config.setTestOnBorrow(Boolean.valueOf(bundle.getString("redis.pool.testOnBorrow")));
        config.setTestOnReturn(Boolean.valueOf(bundle.getString("redis.pool.testOnReturn")));

        pool = new JedisPool(config, bundle.getString("redis.ip"),
                Integer.valueOf(bundle.getString("redis.port")), 600);





    }

    public static void main(String[] args){

        // 从池中获取一个Jedis对象
        Jedis jedis = pool.getResource();
        //System.out.println(jedis.get("redis.pool.maxTotal"));
        System.out.println(jedis);

        // 释放对象池
        //切记，最后使用后，释放Jedis对象
        //pool.returnResource(jedis); 高版本中官方废弃了此方法，可用如下方法释放
        try {
            jedis = pool.getResource();
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }
}
