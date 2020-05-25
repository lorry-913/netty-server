package com.midea.util;

import java.util.Iterator;


import java.util.List;
import java.util.Set;

import com.midea.common.Constants;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class JedisUtil {
	// private final Logger logger = Logger.getLogger(JedisUtil.class);

	private static JedisPool jedisPool = null;

	public static void init() {
		JedisPoolConfig config = new JedisPoolConfig();
		config.setMaxWaitMillis(Long.valueOf(Constants.getRedisPropertiesValue("redis.pool.maxWait")));
		config.setMaxTotal(Integer.parseInt(Constants.getRedisPropertiesValue("redis.pool.maxActive")));
		// config.setMaxActive(Constants.getRedisPropertiesValue("redis.pool.maxActive"));
		config.setMaxIdle(Integer.parseInt(Constants.getRedisPropertiesValue("redis.pool.maxIdle")));
		config.setTestOnBorrow(Boolean.parseBoolean(Constants.getRedisPropertiesValue("redis.pool.testOnBorrow")));
		config.setTestOnReturn(Boolean.parseBoolean(Constants.getRedisPropertiesValue("redis.pool.testOnReturn")));
		// redis如果设置了密码：
		String redisPassword = Constants.getRedisPropertiesValue("redis.password");
		if (CommonUtils.isStrNotNull(redisPassword)) {
			jedisPool = new JedisPool(config, Constants.getRedisPropertiesValue("redis.ip"),
					Integer.parseInt(Constants.getRedisPropertiesValue("redis.port")), 10000,
					Constants.getRedisPropertiesValue("redis.password"));
		} else {
			jedisPool = new JedisPool(config, Constants.getRedisPropertiesValue("redis.ip"),
					Integer.parseInt(Constants.getRedisPropertiesValue("redis.port")), 10000);
		}
		System.out.println("redis启动...");
		// jedisPool = new JedisPool();
	}

	public static JedisPool getPool() {
		return jedisPool;
	}

	/**
	 * 从jedis连接池中获取获取jedis对象
	 */
	private synchronized static Jedis getJedis() {
		try {
			if (jedisPool != null) {
				Jedis resource = jedisPool.getResource();
				return resource;
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 回收jedis
	 */
	public static void returnJedis(Jedis jedis) {
		if (jedis != null)
			jedis.close();
	}

	/**
	 * @deprecated
	 * @param key
	 * @param value
	 * @param seconds
	 */
	public static void set(String key, Object value, int seconds) {
		Jedis jedis = JedisUtil.getJedis();
		try {
			if (!isBlank(key) && seconds > 0) {
				jedis.setex(key.getBytes(), seconds, SerializeUtil.serialize(value));
			}
		} catch (Exception e) {
			System.out.println("redis插入异常" + e);
		} finally {
			JedisUtil.returnJedis(jedis);
		}
	}

	/**
	 * @deprecated
	 * @param key
	 */
	public static Object getObj(String key) {
		Jedis jedis = null;
		Object ob = null;
		try {
			jedis = JedisUtil.getJedis();
			byte[] bits = jedis.get(key.getBytes());
			if (null != bits && bits.length >= 0) {
				ob = SerializeUtil.unserialize(bits);
			}
		} catch (Exception e) {
			System.out.println("redis查询异常" + e);
		} finally {
			JedisUtil.returnJedis(jedis);
		}
		return ob;
	}

	public static String getStringCache(String key) {
		Jedis jedis = JedisUtil.getJedis();
		String result = null;
		try {
			if (!isBlank(key)) {
				result = jedis.get(key);
			}
		} catch (Exception e) {
			System.out.println("redis查询异常" + e);
		} finally {
			JedisUtil.returnJedis(jedis);
		}

		return result;
	}

	public static boolean putStringCache(String key, String value) {
		Jedis jedis = JedisUtil.getJedis();
		boolean result = false;
		try {
			if (!isBlank(key)) {
				jedis.set(key, value);
				result = true;
			}
		} catch (Exception e) {
			System.out.println("redis插入异常" + e);
		} finally {
			JedisUtil.returnJedis(jedis);
		}

		return result;
	}

	public static void putStringCacheWithExpireTime(String key, String value, final int expireTime) {
		Jedis jedis = JedisUtil.getJedis();
		try {
			if (!isBlank(key) && expireTime > 0) {
				jedis.setex(key, expireTime, value);
			}
		} catch (Exception e) {
			System.out.println("redis插入异常" + e);
		} finally {
			JedisUtil.returnJedis(jedis);
		}
	}

	public static <T> boolean putCache(String key, T obj) {
		Jedis jedis = JedisUtil.getJedis();
		boolean result = false;
		try {
			if (!isBlank(key)) {
				jedis.set(key.getBytes(), ProtoStuffSerializerUtil.serialize(obj));
				result = true;
			}
		} catch (Exception e) {
			System.out.println("redis插入异常" + e);
		} finally {
			JedisUtil.returnJedis(jedis);
		}

		return result;
	}

	public static <T> void putCacheWithExpireTime(String key, T obj, final int expireTime) {
		Jedis jedis = JedisUtil.getJedis();
		try {
			if (!isBlank(key) && expireTime > 0) {
				jedis.setex(key.getBytes(), expireTime, ProtoStuffSerializerUtil.serialize(obj));
			}
		} catch (Exception e) {
			System.out.println("redis插入异常" + e);
		} finally {
			JedisUtil.returnJedis(jedis);
		}
	}

	public static <T> boolean putListCache(String key, List<T> objList) {
		Jedis jedis = JedisUtil.getJedis();
		boolean result = false;
		try {
			if (!isBlank(key)) {
				jedis.set(key.getBytes(), ProtoStuffSerializerUtil.serializeList(objList));
				result = true;
			}
		} catch (Exception e) {
			System.out.println("redis插入异常" + e);
		} finally {
			JedisUtil.returnJedis(jedis);
		}

		return result;
	}

	public static <T> void putListCacheWithExpireTime(String key, List<T> objList, final int expireTime) {
		Jedis jedis = JedisUtil.getJedis();
		try {
			if (!isBlank(key) && expireTime > 0) {
				jedis.setex(key.getBytes(), expireTime, ProtoStuffSerializerUtil.serializeList(objList));
			}
		} catch (Exception e) {
			System.out.println("redis插入异常" + e);
		} finally {
			JedisUtil.returnJedis(jedis);
		}
	}

	public static <T> T getCache(final String key, Class<T> targetClass) {
		Jedis jedis = JedisUtil.getJedis();
		byte[] result = null;
		try {
			if (!isBlank(key)) {
				result = jedis.get(key.getBytes());
			}
		} catch (Exception e) {
			System.out.println("redis查询异常" + e);
		} finally {
			JedisUtil.returnJedis(jedis);
		}

		if (result == null) {
			return null;
		}
		return ProtoStuffSerializerUtil.deserialize(result, targetClass);
	}

	public static <T> List<T> getListCache(final String key, Class<T> targetClass) {
		Jedis jedis = JedisUtil.getJedis();
		byte[] result = null;
		try {
			if (!isBlank(key)) {
				result = jedis.get(key.getBytes());
			}
		} catch (Exception e) {
			System.out.println("redis查询异常" + e);
		} finally {
			JedisUtil.returnJedis(jedis);
		}

		if (result == null) {
			return null;
		}
		return ProtoStuffSerializerUtil.deserializeList(result, targetClass);
	}

	/**
	 * 模糊删除key
	 *
	 * @param pattern
	 */
	public static void deleteCacheWithPattern(String pattern) {
		Jedis jedis = JedisUtil.getJedis();
		try {
			if (!isBlank(pattern)) {
				Iterator<String> it = jedis.keys(pattern).iterator();
				while (it.hasNext()) {
					jedis.del(it.next().getBytes());
				}
			}
		} catch (Exception e) {
			System.out.println("redis删除异常" + e);
		} finally {
			JedisUtil.returnJedis(jedis);
		}
	}

	/**
	 * 精确删除key
	 *
	 * @param key
	 */
	public static void deleteCache(String key) {
		Jedis jedis = JedisUtil.getJedis();
		try {
			if (!isBlank(key)) {
				jedis.del(key.getBytes());
			}
		} catch (Exception e) {
			System.out.println("redis删除异常" + e);
		} finally {
			JedisUtil.returnJedis(jedis);
		}
	}

	public static void batchDel(String key) {
		Jedis jedis = JedisUtil.getJedis();
		try {
			if (!isBlank(key)) {
				Set<String> set = jedis.keys(key + "*");
				Iterator<String> it = set.iterator();
				while (it.hasNext()) {
					String keyStr = it.next();
//					System.out.println(keyStr);
					jedis.del(keyStr);
				}
			}
		} catch (Exception e) {
			System.out.println("redis删除异常" + e);
		} finally {
			JedisUtil.returnJedis(jedis);
		}

	}

	public static boolean isBlank(String str) {
		return str == null || "".equals(str.trim());
	}

}
