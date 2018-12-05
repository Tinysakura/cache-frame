package com.cfh.cacheframe.adapter.client;

import com.cfh.cacheframe.adapter.CacheClient;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @Author: chenfeihao@corp.netease.com
 * @Date: 2018/12/4
 */

public class FIFOCacheClient<K, V> extends ConcurrentHashMap<K, V>implements CacheClient{

    // 使用ConcurrentLinkedQueue记录元素进入容器的顺序
    private ConcurrentLinkedQueue<V> linkedQueue;

    public FIFOCacheClient(int maxCapacity) {
        super(maxCapacity);
        linkedQueue = new ConcurrentLinkedQueue<>();
    }

    /**
     * @param key
     * @param value
     * @return
     */
    @Override
    public V put(K key, V value) {
        synchronized (this) {
            super.remove(linkedQueue.poll());
            linkedQueue.add(value);
        }

        return super.put(key, value);
    }
}