package com.cfh.cacheframe.adapter;

/**
 * @Author: chenfeihao@corp.netease.com
 * @Date: 2018/11/28
 * 缓存适配器接口，使用适配器模式消除各个分布式数据库中间件的差异
 */

public interface CacheAdaptor {
    // 添加缓存
    public boolean insert(String key, Object value);

    // 更新缓存
    public boolean update(String key, Object value);

    // 删除缓存
    public boolean delete(String key);

    // 设置缓存过期时间
    public boolean setExpireTime(String key, Long mill);
}