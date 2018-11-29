package com.cfh.cacheframe.adapter.adaptor;

import com.cfh.cacheframe.adapter.CacheAdaptor;
import redis.clients.jedis.JedisPool;

/**
 * @Author: chenfeihao@corp.netease.com
 * @Date: 2018/11/29
 * 使用redis作为缓存中间件的适配器类
 */

public class RedisCacheAdaptor implements CacheAdaptor {

    JedisPool jedisPool = new JedisPool();

    public RedisCacheAdaptor() {

    }

    @Override
    public Object get(String key) {
        return false;
    }

    @Override
    public boolean insert(String key, Object value) {
        return false;
    }

    @Override
    public boolean update(String key, Object value) {
        return false;
    }

    @Override
    public boolean delete(String key) {
        return false;
    }

    @Override
    public boolean setExpireTime(String key, Long mill) {
        return false;
    }

    @Override
    public void setClient(Object client) {
        this.jedisPool = (JedisPool)client;
    }
}