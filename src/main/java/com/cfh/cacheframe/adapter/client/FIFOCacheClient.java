package com.cfh.cacheframe.adapter.client;

import com.cfh.cacheframe.adapter.CacheClient;
import com.cfh.cacheframe.adapter.RejectHandler;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @Author: chenfeihao@corp.netease.com
 * @Date: 2018/12/4
 */
public class FIFOCacheClient<K, V> extends ConcurrentHashMap<K, V>implements CacheClient{

    // 使用ConcurrentLinkedQueue记录元素进入容器的顺序
    private ConcurrentLinkedQueue<V> linkedQueue;

    private volatile long memSize;

    private RejectHandler<K, V> rejectHandler;

    public FIFOCacheClient(int maxCapacity) {
        super(maxCapacity);
        linkedQueue = new ConcurrentLinkedQueue<>();
    }

    public FIFOCacheClient(int maxCapacity, Long memSize) {
        super(maxCapacity);
        linkedQueue = new ConcurrentLinkedQueue<>();
        this.memSize = memSize;
    }

    public RejectHandler<K, V> getRejectHandler() {
        return rejectHandler;
    }

    public void setRejectHandler(RejectHandler<K, V> rejectHandler) {
        this.rejectHandler = rejectHandler;
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