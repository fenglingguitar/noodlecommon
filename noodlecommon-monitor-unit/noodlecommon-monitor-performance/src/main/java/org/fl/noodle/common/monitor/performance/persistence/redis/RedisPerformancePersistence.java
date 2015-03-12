package org.fl.noodle.common.monitor.performance.persistence.redis;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.fl.noodle.common.monitor.performance.persistence.PerformancePersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.fl.noodle.common.util.json.JsonTranslator;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.exceptions.JedisConnectionException;

public class RedisPerformancePersistence extends JedisPoolConfig implements PerformancePersistence {
	
	private final static Logger logger = LoggerFactory.getLogger(RedisPerformancePersistence.class);
	
	private JedisPool jedisPool;
	
	private String ip;
	private int port;
	private int timeout;
	
	public void start() throws Exception {
		jedisPool = new JedisPool(this, ip, port, timeout);
	}
	
	public void destroy() throws Exception {
		jedisPool.destroy();
	}
	
	public <T> List<T> queryList(String keyName, double min, double max, Class<T> clazz) throws Exception {
		
		Set<String> valueSet = null;
		
		Jedis jedis = null;
		
		try {
			jedis = jedisPool.getResource();
		} catch (JedisConnectionException e) {
			if (logger.isErrorEnabled()) {
				logger.error("queryList -> jedisPool.getResource -> ip:{}, port:{}, keyName{}, min:{}, max:{} -> Exception:{}", ip, port, keyName, min, max, e.getMessage());
			}
			throw e;
		}
		
		try {
			valueSet = jedis.zrangeByScore(keyName, min, max);		
		} catch (Exception e) {
			if (logger.isErrorEnabled()) {
				logger.error("queryList -> jedis.zrangeByScore -> ip:{}, port:{}, keyName{}, min:{}, max:{} -> Exception:{}", ip, port, keyName, min, max, e.getMessage());
			}
			throw e;
		} finally {
			jedisPool.returnResource(jedis);
		}
		
		List<T> result = new ArrayList<T>();

		try {
			if (valueSet != null) {				
				for (String value : valueSet) {
					T t = JsonTranslator.fromString(value, clazz);
					result.add(t);
				}	
			}
		} catch (Exception e) {
			if (logger.isErrorEnabled()) {
				logger.error("queryList -> JsonTranslator.fromString -> ip:{}, port:{}, keyName{}, min:{}, max:{} -> Exception:{}", ip, port, keyName, min, max, e.getMessage());
			}
		}
		
		return result;
	}

	public void insert(String keyName, double score, Object vo) throws Exception {
		
		String member = null;
		
		try {
			member = JsonTranslator.toString(vo);
		} catch (Exception e) {
			if (logger.isErrorEnabled()) {
				logger.error("insert -> JsonTranslator.toString -> ip:{}, port:{}, keyName{}, score:{} -> Exception:{}", ip, port, keyName, score, e.getMessage());
			}
			return;
		}
		
		Jedis jedis = null;
		
		try {
			jedis = jedisPool.getResource();
		} catch (JedisConnectionException e) {
			if (logger.isErrorEnabled()) {
				logger.error("insert -> jedisPool.getResource -> ip:{}, port:{}, keyName{}, score:{} -> Exception:{}", ip, port, keyName, score, e.getMessage());
			}
			throw e;
		}
		
		try {		
			jedis.zadd(keyName, score, member);
		} catch (Exception e) {
			if (logger.isErrorEnabled()) {
				logger.error("insert -> jedis.zadd -> ip:{}, port:{}, keyName{}, score{} -> Exception:{}", ip, port, keyName, score, e.getMessage());
			}
			throw e;
		} finally {
			jedisPool.returnResource(jedis);
		}

	}

	public long deletes(String keyName, double min, double max) throws Exception {
		
		Jedis jedis = null;
		
		try {
			jedis = jedisPool.getResource();
		} catch (JedisConnectionException e) {
			if (logger.isErrorEnabled()) {
				logger.error("deletes -> jedisPool.getResource -> ip:{}, port:{}, keyName{}, min:{}, max:{} -> Exception:{}", ip, port, keyName, min, max, e.getMessage());
			}
			throw e;
		}
		
		try {
			return jedis.zremrangeByScore(keyName, min, max);
		} catch (Exception e) {
			logger.error("deletes -> jedis.zremrangeByScore -> ip:{}, port:{}, keyName{}, min:{}, max:{} -> Exception:{}", ip, port, keyName, min, max, e.getMessage());
			throw e;
		} finally {
			jedisPool.returnResource(jedis);
		}
	}


	@Override
	public Set<String> getKeys() throws Exception {
		
		Jedis jedis = null;
		
		try {
			jedis = jedisPool.getResource();
		} catch (JedisConnectionException e) {
			if (logger.isErrorEnabled()) {
				logger.error("deletes -> jedisPool.getResource -> ip:{}, port:{}, -> Exception:{}", ip, port, e.getMessage());
			}
			throw e;
		}
		
		Set<String> keysSet = null;
		
		try {
			keysSet = jedis.keys("KEY-*");
		} catch (Exception e) {
			if (logger.isErrorEnabled()) {
				logger.error("deletes -> jedisPool.getResource -> ip:{}, port:{}, -> Exception:{}", ip, port, e.getMessage());
			}
			throw e;
		} finally {
			jedisPool.returnResource(jedis);
		}
		
		return keysSet;
	}
	
	public void setIp(String ip) {
		this.ip = ip;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}
}
