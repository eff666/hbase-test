package com.dennyac.HbaseTest.redis;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import redis.clients.jedis.Jedis;

/**
 * redis 常用操作类
 *
 */
public class RedisCommUtil {

    public static boolean REDIS_STATUS = true;
	/**
	 * 将值存入redis中
	 * @param key  redis的键
	 * @param value redis的值
	 */
	public static void putOutofTime(String key,String value) {
	    //判断redis是否可用 
	    if(!REDIS_STATUS || StringUtils.isEmpty(key) || StringUtils.isEmpty(value)) {
	        return ;
	    }
	    //end
		Jedis jedis = null ;
		try{
			jedis = RedisHolder.getJedis() ;
			jedis.hset(key, "content", value);
			jedis.hset(key, "createtime",String.valueOf(System.currentTimeMillis()));//设置初始化时间
			jedis.hset(key, "times", String.valueOf(1));//设置访问次数
			jedis.expire(key, 2*24*60*60);//设置过期时间为300s
		}finally{
			if(null != jedis)
				RedisHolder.returnResource(jedis);
		}
	}
	/**
	 * 将值存入redis中
	 * @param key  redis的键
	 * @param value redis的值
	 * @param min 缓存时长（分钟）
	 */
	public static void putOutofTime(String key,String value,int min) {
	    //判断redis是否可用 
	    if(!REDIS_STATUS || StringUtils.isEmpty(key) || StringUtils.isEmpty(value) || min==0) {
	        return ;
	    }
	    //end
		Jedis jedis = null ;
		try{
			jedis = RedisHolder.getJedis() ;
			jedis.hset(key, "content", value);
			jedis.hset(key, "createtime",String.valueOf(System.currentTimeMillis()));//设置初始化时间
			jedis.hset(key, "times", String.valueOf(1));//设置访问次数
			jedis.expire(key, min*60);//设置过期时间为300s
		}finally{
			if(null != jedis)
				RedisHolder.returnResource(jedis);
		}
	}
	
	/**
	 * 将值存入redis中，过期时间为1天
	 * @param key  redis的键
	 * @param value redis的值
	 */
	public static void putOutofTimeOneDay(String key,String value) {
	    //判断redis是否可用 
	   if(!REDIS_STATUS || StringUtils.isEmpty(key) || StringUtils.isEmpty(value)) {
	        return;
	    }
	    //end
		Jedis jedis = null ;
		try{
			jedis = RedisHolder.getJedis() ;
			jedis.hset(key, "content", value);
			jedis.hset(key, "createtime",String.valueOf(System.currentTimeMillis()));//设置初始化时间
			jedis.hset(key, "times", String.valueOf(1));//设置访问次数
			jedis.expire(key, 24*60*60);//设置过期时间为1天
		}finally{
			if(null != jedis)
				RedisHolder.returnResource(jedis);
		}
	}
	
	/**
	 * 获取缓存中键的值,两天失效
	 * @param key redis的键
	 */
	public static String getOfOutimeOneday(String key){
	    //判断redis是否可用
	    if(!REDIS_STATUS) {
            return null;
        }
	    //end
		Jedis jedis = null ;
		try{
			jedis = RedisHolder.getJedis() ;
			String response = jedis.hget(key, "content");
			if(null == response){
				return null ;
			}else {
				jedis.hincrBy(key, "times", 1);
				String createtime = jedis.hget(key,"createtime");
				if(null != createtime){
					long start = Long.parseLong(createtime) ;
					long now = System.currentTimeMillis() ;
					long i = (now-start)/(1000*60*60*24*2) ;
					//long i = (now-start)/(1000) ;
					if(i > 1){ // 超过一天的缓存数据，马上失效
						jedis.expire(key, -1);
						//失效后直接返回，无需往下执行
						return response;
					}
				}
				return response ;
			}
			
		}finally{
			if(null != jedis)
				RedisHolder.returnResource(jedis);
		}
	}
	
	/**
	 * 自定义失效时间
	 * @param key redis的键
	 * @param min 失效时长
	 */
	public static String getOfOutimeOneday(String key,int min){
	    //判断redis是否可用
	    if(!REDIS_STATUS) {
            return null;
        }
	    //end
		Jedis jedis = null ;
		try{
			jedis = RedisHolder.getJedis() ;
			String response = jedis.hget(key, "content");
			if(null == response){
				return null ;
			}else {
				jedis.hincrBy(key, "times", 1);
				String createtime = jedis.hget(key,"createtime");
				if(null != createtime){
					long start = Long.parseLong(createtime) ;
					long now = System.currentTimeMillis() ;
					long i = (now-start)/(1000*60*min) ;
					//long i = (now-start)/(1000) ;
					if(i > 1){ // 超过一天的缓存数据，马上失效
						jedis.expire(key, -1);
						//失效后直接返回，无需往下执行
						return response;
					}
				}
				return response ;
			}
			
		}finally{
			if(null != jedis)
				RedisHolder.returnResource(jedis);
		}
	}
	
	
	/**
	 * 获取缓存中键的值
	 * @param key redis的键
	 */
	public static boolean clearCache(String key){
		Jedis jedis = null ;
		try{
			jedis = RedisHolder.getJedis() ;
			String response = jedis.hget(key, "content");
			if(null == response){
				return false ;
			}else {
				jedis.del(key);
				return true;
			}
			
		}finally{
			if(null != jedis)
				RedisHolder.returnResource(jedis);
		}
	}
	
	/**
	 * 删除缓存
	 * @param key redis的键
	 */
	public static boolean delCache(String key){
		Jedis jedis = null ;
		try{
			jedis = RedisHolder.getJedis() ;
			jedis.del(key);
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}finally{
			if(null != jedis)
				RedisHolder.returnResource(jedis);
		}
		return true;
	}
	
	/**
	 * 以map形式缓存对像信息
	 * @param objName 对像名称
	 * @param value 值信息
	 */
	public static void putObject(String objName,Map<String,String> value){
		if(null == objName || "".equals(objName) || null == value){
			return;
		}
		Jedis jedis = null ;
		try {
			jedis = RedisHolder.getJedis() ;
			jedis.hmset(objName, value);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(null != jedis)
				RedisHolder.returnResource(jedis);
		}
	}
	
	/**
	 * 判断key是否存在
	 * @param key key值
	 * @return
	 */
	public static boolean isexistKey(String key){
		boolean res = false;
		if(null == key || "".equals(key)){
			return res;
		}
		Jedis jedis = null ;
		try {
			jedis = RedisHolder.getJedis() ;
			res = jedis.exists(key);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(null != jedis)
				RedisHolder.returnResource(jedis);
		}
		return res;
	}
	/**
	 * 根据key 与 字段名称取值
	 * @param key
	 * @param vname
	 * @return
	 */
	public static String getValueByField(String key,String vname){
		String res = "";
		if(null == key || null == vname || "".equals(key) || "".equals(vname)){
			return res;
		}
		Jedis jedis = null ;
		try {
			jedis = RedisHolder.getJedis() ;
			res = jedis.hget(key, vname);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(null != jedis)
				RedisHolder.returnResource(jedis);
		}
		return res;
	}
	
	/**
	 * 根据key 与 字段名称取多值
	 * @param key
	 * @return
	 */
	public static List<String> getValuesByFields(String key,String ...fields){
		List<String> res = null;
		if(null == key || null == fields || "".equals(key)){
			return res;
		}
		Jedis jedis = null ;
		try {
			jedis = RedisHolder.getJedis() ;
			if(null !=jedis){
				res = jedis.hmget(key, fields);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(null != jedis)
				RedisHolder.returnResource(jedis);
		}
		return res;
	}
	/**
	 * 普通存储数据
	 * @param key
	 * @param value
	 * @return
	 */
	public static int putData(String key,String value){
		if(null == key || null == value){
			return 0;
		}
		Jedis jedis = null ;
		try {
			jedis = RedisHolder.getJedis() ;
			if(null !=jedis){
				 jedis.set(key, value);
			}else{
				return 3;
			}
			 return 1;
		} catch (Exception e) {
			e.printStackTrace();
			return 2;
		}finally{
			if(null != jedis)
				RedisHolder.returnResource(jedis);
		}
	}
	
	/**
	 * 普通存储数据
	 * @param key
	 * @return
	 */
	public static String getData(String key){
		if(null == key){
			return null;
		}
		Jedis jedis = null ;
		try {
			jedis = RedisHolder.getJedis() ;
			if(null !=jedis){
				return jedis.get(key);
			}else{
				return "";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}finally{
			if(null != jedis)
				RedisHolder.returnResource(jedis);
		}
	}
	
	/**
	 * 根据key模糊匹配数据
	 * @param key
	 * @return
	 */
	public static List<String> getDataOfLike(String key){
		Jedis jedis = null ;
		List<String> list = new ArrayList<String>();
		try {
			jedis = RedisHolder.getJedis() ;
			if(null !=jedis){
				Set<String> keys = jedis.keys(key);
				Iterator<String> iterator = keys.iterator();
				while(iterator.hasNext()){
					String res = getData(iterator.next());
					if(null != res && !"null".equals(res)){
						list.add(res);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(null != jedis)
				RedisHolder.returnResource(jedis);
		}
		return list;
	}
	
	/**
	 * 根据key模糊匹配数据
	 * @param key
	 * @return
	 */
	public static long countData(String key){
		Jedis jedis = null ;
		long count = 0;
		try {
			jedis = RedisHolder.getJedis() ;
			if(null !=jedis){
				if(StringUtils.isEmpty(key)){
					count = jedis.dbSize();
				}else{
					count = jedis.keys(key).size();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(null != jedis)
				RedisHolder.returnResource(jedis);
		}
		return count;
	}
	
	/**
	 * 根据key模糊匹配数据，有过期时长限制
	 * @param key
	 * @return
	 */
	public static List<String> getDataOfLikeByOutTime(String key){
		Jedis jedis = null ;
		List<String> list = new ArrayList<String>();
		try {
			jedis = RedisHolder.getJedis() ;
			if(null !=jedis){
				Set<String> keys = jedis.keys(key);
				Iterator<String> iterator = keys.iterator();
				while(iterator.hasNext()){
					String res = getOfOutimeOneday(iterator.next());
					if(null != res && !"null".equals(res)){
						list.add(res);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(null != jedis)
				RedisHolder.returnResource(jedis);
		}
		return list;
	}
	
	/**
	 * 根据key模糊匹配数据
	 * @param key
	 * @return
	 */
	public static List<String> getDataOfLike2OutTime(String key,int min){
		Jedis jedis = null ;
		List<String> list = new ArrayList<String>();
		try {
			jedis = RedisHolder.getJedis() ;
			if(null !=jedis){
				Set<String> keys = jedis.keys(key);
				Iterator<String> iterator = keys.iterator();
				while(iterator.hasNext()){
					String res = getOfOutimeOneday(iterator.next(),min);
					if(null != res && !"null".equals(res)){
						list.add(res);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(null != jedis)
				RedisHolder.returnResource(jedis);
		}
		return list;
	}
	/**
	 * redis分页查询
	 * @param pageFlag redisKey中的特殊标识符
	 * @param pageNo 当前页数
	 * @param pageSize 每页显示的记录数
	 * @return 查询结果集合
	 */
	public static List<String> queryDataByPage(String pageFlag,int pageNo,int pageSize){
		List<String> list = new ArrayList<String>();
		if(StringUtils.isEmpty(pageFlag)){
			return list;
		}
		int start = pageSize * (pageNo-1);
		int end = start + pageSize - 1;
		Jedis jedis = null ;
		try {
			jedis = RedisHolder.getJedis() ;
			if(null !=jedis){
				list = jedis.lrange(pageFlag, start, end);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(null != jedis)
				RedisHolder.returnResource(jedis);
		}
		return list;
	}
	
	/**
	 * 分页数据录入
	 * @param datas 数据集合
	 * @return
	 */
	public boolean rpushData(List<Map<String,String>> datas){
		boolean res = false;
		if(null == datas){
			return res;
		}
		Jedis jedis = null ;
		try {
			jedis = RedisHolder.getJedis() ;
			if(null !=jedis){
				for(Map<String, String> map : datas){
					for(Map.Entry<String, String> kv : map.entrySet()){
						jedis.rpush(kv.getKey(), kv.getValue());
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(null != jedis)
				RedisHolder.returnResource(jedis);
		}
		return res;
	}
	
	
	public static void main(String[] args) {
		/*Map<String,String> map = new HashMap<String, String>();
		map.put("speed", "65");
		map.put("color", "01");
		map.put("type", "02");
		RedisCommUtil.putObject("辽A2210", map);
		map.put("type", "03");
		RedisCommUtil.putObject("辽A2210", map);*/
		/*RedisCommUtil.putOutofTime("aaa1", "bbb1");
		RedisCommUtil.putOutofTime("aaa2", "bbb2");
		RedisCommUtil.putOutofTime("aaa3", "bbb3");
		RedisCommUtil.putOutofTime("aaa4", "bbb4");
		RedisCommUtil.putOutofTime("aaa5", "bbb5");*/
		//RedisCommUtil.putOutofTime("彭程", "彭程");
		//System.out.println(RedisCommUtil.getDataOfLike("A35952_02_f35ededeb91948fca03ba63743d31907*"));
		/*System.out.println("键值是否存在："+RedisCommUtil.isexistKey("210102600003"));
		System.out.println("颜色："+RedisCommUtil.getValue("210102600003", "NAME"));*/
		//System.out.println("多值："+RedisCommUtil.getValues("辽A2210", "speed","type"));
		/*RedisCommUtil.putData("aaaa1", "1");
		RedisCommUtil.putData("aaaa2", "2");
		RedisCommUtil.putData("aaaa3", "3");
		RedisCommUtil.putData("aaaa4", "4");
		*/
		/**
		-----listKey----f35ededeb91948fca03ba63743d31907_A35952
		-----listKey----f35ededeb91948fca03ba63743d31907_A35952
		-----did----2512205852_f35ededeb91948fca03ba63743d31907
		-----did----2512205852_f35ededeb91948fca03ba63743d31907
		-----did----1212205855_f35ededeb91948fca03ba63743d31907
		-----did----1212205855_f35ededeb91948fca03ba63743d31907
		 */
		//System.out.println(RedisCommUtil.delCache("A359522101040000012101046000021812591218135800"));
		//System.out.println(RedisCommUtil.delCache("A35952_02_f35ededeb91948fca03ba63743d31907_121220"));
		//System.out.println("---"+RedisCommUtil.getData("A359522101040000012101026000081812591218125800"));
		
		//RedisCommUtil.putData("aaaa1", "6");
		//System.out.println(RedisCommUtil.isexistKey("A35952_02_f35ededeb91948fca03ba63743d31907_121220"));
		//System.out.println(RedisCommUtil.getDataOfLike("aaaa*"));
		//System.out.println(RedisCommUtil.getData("aaaa1"));
		
		/*RedisCommUtil.putOutofTime("kkk234", "12345", 5);
		RedisCommUtil.putOutofTime("kkk456", "12345", 5);
		RedisCommUtil.putOutofTime("kkk767", "12345", 5);
		System.out.println("~~~~~~~~~~"+RedisCommUtil.getDataOfLike2OutTime("kkk*", 5).size());
		*/
		//System.out.println("~~~~~~~~~~"+RedisCommUtil.getDataOfLike2OutTime("A35952_02_f35ededeb91948fca03ba63743d31907*", 100));
		
		//System.out.println(RedisCommUtil.getDataOfLike("CLBMDXX_*"));
		
		System.out.println("count------"+RedisCommUtil.countData("CLBMDXX_*"));
		
		/*A35952_02_f35ededeb91948fca03ba63743d31907_251220
		A35952_02_f35ededeb91948fca03ba63743d31907_121220*/
	}
}
