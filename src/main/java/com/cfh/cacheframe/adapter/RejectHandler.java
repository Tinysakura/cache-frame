package com.cfh.cacheframe.adapter;

/**
 * @Author: chenfeihao@corp.netease.com
 * @Date: 2018/12/5
 * 拒绝策略接口
 */
public interface RejectHandler<K, V> {
    public boolean reject(K key, V value);

    public void setCacheClient(CacheClient cacheClient);
}
