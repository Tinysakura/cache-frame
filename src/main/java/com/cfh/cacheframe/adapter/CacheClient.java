package com.cfh.cacheframe.adapter;

/**
 * @Author: chenfeihao@corp.netease.com
 * @Date: 2018/12/4
 * 标识接口
 */

public interface CacheClient {
    // 获取缓存
    public Object get(String key);

    // 添加缓存
    public boolean insert(String key, Object value);

    // 更新缓存
    public boolean update(String key, Object value);

    // 删除缓存
    public boolean delete(String key);
}