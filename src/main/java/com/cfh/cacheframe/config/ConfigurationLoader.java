package com.cfh.cacheframe.config;

import com.cfh.cacheframe.adapter.CacheAdaptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @Author: chenfeihao@corp.netease.com
 * @Date: 2018/11/28
 * 框架配置
 */
@Configuration
public class ConfigurationLoader {
    Logger log = LoggerFactory.getLogger(ConfigurationLoader.class);

    // 注入jedisPool这个bean
    @Bean(name = "jedis.pool")
    @Autowired
    public JedisPool jedisPool(@Qualifier("jedis.pool.config") JedisPoolConfig config,
                               @Value("${jedis.pool.host}") String host,
                               @Value("${jedis.pool.port}") int port) {
        log.info("init jedisPool");
        return new JedisPool(config, host, port);
    }

    // 注入jedisPoolConfig这个bean
    @Bean(name = "jedis.pool.config")
    public JedisPoolConfig jedisPoolConfig(@Value("${jedis.pool.config.maxTotal}") int maxTotal,
                                           @Value("${jedis.pool.config.maxIdle}") int maxIdle,
                                           @Value("${jedis.pool.config.maxWaitMillis}") int maxWaitMillis) {
        log.info("init jedisPoolConfig");
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(maxTotal);
        config.setMaxIdle(maxIdle);
        config.setMaxWaitMillis(maxWaitMillis);
        return config;
    }

    // 注入CacheAdaptor
    @Bean(name = "cacheAdaptor")
    public CacheAdaptor cacheAdaptor(@Qualifier("jedis.pool") JedisPool jedisPool,
                                     @Value("${cache.adaptor.type}") String cacheAdaptorType) {
        log.info("init cacheAdaptor");

        try {
            CacheAdaptor cacheAdaptor = (CacheAdaptor) Class.forName(cacheAdaptorType).newInstance();
            cacheAdaptor.setClient(jedisPool);

            return cacheAdaptor;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }
}