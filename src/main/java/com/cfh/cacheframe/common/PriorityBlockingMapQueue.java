package com.cfh.cacheframe.common;

import com.cfh.cacheframe.adapter.client.LFUCacheClient;

import java.util.Comparator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * @Author: chenfeihao@corp.netease.com
 * @Date: 2018/12/4
 * 扩展PriorityBlockingMapQueue使其对特定的元素可以索引
 */

public class PriorityBlockingMapQueue<T> extends PriorityBlockingQueue<T>{
    private ConcurrentHashMap<Object, T> safeMap;

    public PriorityBlockingMapQueue(int maxCapacity, Comparator<? super T> comparator) {
        super(maxCapacity, comparator);
        safeMap = new ConcurrentHashMap<>();
    }

    @Override
    public synchronized boolean add(T value) {
        if (! (value instanceof LFUCacheClient.Entry)) {
            return false;
        }

        safeMap.put(((LFUCacheClient.Entry) value).getKey(), value);
        return super.add(value);
    }

    public void increaseHitFrequency(Object key) {
        LFUCacheClient.Entry value = (LFUCacheClient.Entry) safeMap.get(key);
        value.setHitFrequency(value.getHitFrequency() + 1);
    }
}