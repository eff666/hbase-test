package com.dennyac.HbaseTest.redis;

import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisHolder {

    private static Logger LOGGER = LoggerFactory.getLogger(RedisHolder.class);
	private static JedisPool pool = null ;
	
	private static RedisHolder instance = new RedisHolder() ;
	/**
	 * 获取Jedis实例
	 * @return Jedis实例
	 */
	public static synchronized Jedis getJedis(){
		if(null == pool){
			synchronized (instance) {
				if(null == pool){
					AbstractPool abstractPool = new AbstractPool() ; 
					pool = abstractPool.pool ;
				}
			}
		}
		try {
		    return pool.getResource() ;
        } catch (Exception e) {
            LOGGER.error("redis异常", e);
            return null;
        }
		
	}
	
	/**
	 * 释放Jedis实例
	 * @param redis
	 */
	public static void returnResource(Jedis redis)
    {
        if (null != redis) {
            pool.returnResource(redis);
        }
    }
    
	/**
	 * 获取Jedis连接池
	 * @return
	 */
    public static JedisPool getPool() 
    {
        return  pool ;
    }
    /**
     * 销毁Jedis线程池
     */
    public static void destroy(){
    	if ( null != pool ) {
    		pool.destroy() ;
    		pool = null ;
    	}
    }
	
	/**
	 * Redis连接池  内部类
	 *
	 */
	private static class AbstractPool {
		//Jedis 连接池
		private  JedisPool pool = null;
	    //可用连接实例的最大数目，默认值为8；
	    //如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)。
	    private static int MAX_ACTIVE = 1024;
	    
	    //控制一个pool最多有多少个状态为idle(空闲的)的jedis实例，默认值也是8。
	    private static int MAX_IDLE = 200;
	    
	    //等待可用连接的最大时间，单位毫秒，默认值为-1，表示永不超时。如果超过等待时间，则直接抛出JedisConnectionException；
	    private static int MAX_WAIT = 10000;
	    
	    private static int TIMEOUT = 10000;
	    
	    //在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
	    private static boolean TEST_ON_BORROW = true;
	    
	    private static String HOSTNAME = "";
	    
	    private static int PORT = 0 ;
	    
	    
	    public AbstractPool()  {
	    	Properties properties = new Properties();
	    	try {
				properties.load(RedisHolder.class.getResourceAsStream("/redis-config.properties"));
				MAX_ACTIVE = Integer.parseInt(properties.getProperty("MAX_ACTIVE")) ;
				MAX_IDLE = Integer.parseInt(properties.getProperty("MAX_IDLE")) ;
				MAX_WAIT = Integer.parseInt(properties.getProperty("MAX_WAIT")) ;
				TIMEOUT = Integer.parseInt(properties.getProperty("TIMEOUT")) ;
				HOSTNAME = properties.getProperty("REDIS-HOST");
				PORT = Integer.parseInt(properties.getProperty("REDIS-PORT")) ;
				//System.out.println("IP==="+HOSTNAME+"==========port:"+PORT);
			} catch (IOException e) {
				e.printStackTrace();
				throw new RuntimeException("redis-config配置文件读写出现异常.") ;
			}
	    	
	    	JedisPoolConfig config = new JedisPoolConfig();
            config.setNumTestsPerEvictionRun(-1);
            config.setTestWhileIdle(true);
            // 如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)。
            config.setMaxTotal(MAX_ACTIVE);
            // 控制一个pool最多有多少个状态为idle(空闲的)的jedis实例。
            config.setMaxIdle(MAX_IDLE);
            // 表示当borrow(引入)一个jedis实例时，最大的等待时间，如果超过等待时间，则直接抛出JedisConnectionException；
            config.setMaxWaitMillis(MAX_WAIT*60*60*24);
            config.setTimeBetweenEvictionRunsMillis(MAX_WAIT*60*60*24);
            config.setMinEvictableIdleTimeMillis(MAX_WAIT*60*60*24);
            config.setTestOnBorrow(TEST_ON_BORROW);
            pool = new JedisPool(config, HOSTNAME, PORT);
	    }
	}
}
