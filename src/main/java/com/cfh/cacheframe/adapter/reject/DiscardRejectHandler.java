package com.cfh.cacheframe.adapter.reject;

import com.cfh.cacheframe.adapter.CacheClient;
import com.cfh.cacheframe.adapter.RejectHandler;

/**
 * @Author: chenfeihao@corp.netease.com
 * @Date: 2018/12/5
 */

public class DiscardRejectHandler<K, V> implements RejectHandler<K, V> {

    @Override
    public boolean reject(K key, V value) {
        return false;
    }

    @Override
    public void setCacheClient(CacheClient cacheClient) {

    }
}