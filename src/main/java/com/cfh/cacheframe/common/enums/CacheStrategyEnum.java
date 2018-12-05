package com.cfh.cacheframe.common.enums;

import com.cfh.cacheframe.adapter.client.FIFOCacheClient;
import com.cfh.cacheframe.adapter.client.LRUCacheClient;

/**
 * @Author: chenfeihao@corp.netease.com
 * @Date: 2018/12/4
 * 内存缓存策略的枚举
 */

public enum CacheStrategyEnum {
    /**
     * 先进先出
     */
    FIFO("FIFO", FIFOCacheClient.class),
    /**
     * 最近最久未使用
     */
    LRU("LRU", LRUCacheClient.class),
    /**
     * 最少使用
     */
    LFU("LFU", LRUCacheClient.class)
    ;

    private String strategyName;
    private Class clazz;

    CacheStrategyEnum(String strategyName, Class clazz) {
        this.strategyName = strategyName;
        this.clazz = clazz;
    }

    public String getStrategyName() {
        return strategyName;
    }

    public void setStrategyName(String strategyName) {
        this.strategyName = strategyName;
    }

    public Class getClazz() {
        return clazz;
    }

    public void setClazz(Class clazz) {
        this.clazz = clazz;
    }
}