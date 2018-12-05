package com.cfh.cacheframe.adapter.adaptor;

import com.cfh.cacheframe.adapter.CacheAdaptor;
import com.cfh.cacheframe.adapter.CacheClient;
import com.cfh.cacheframe.adapter.RejectHandler;
import com.cfh.cacheframe.annotation.Cache;
import com.cfh.cacheframe.common.enums.CacheRejectStrategyEnum;
import com.cfh.cacheframe.common.enums.CacheStrategyEnum;
import org.springframework.beans.factory.InitializingBean;

import java.lang.reflect.Constructor;

/**
 * @Author: chenfeihao@corp.netease.com
 * @Date: 2018/12/4
 * 二级缓存（内存缓存）
 */
public class StandByCacheAdaptor implements CacheAdaptor, InitializingBean {
    /**
     * 使用的缓存策略
     */
    private CacheStrategyEnum cacheStrategy;

    /**
     * 缓存拒绝策略
     */
    private CacheRejectStrategyEnum cacheRejectStrategy;

    /**
     * 用于二级缓存的内存(/mb)
     */
    private Long memSize;

    /**
     * 缓存容器的初始容量
     */
    private Long maxCapacity;

    private CacheClient memCacheClient;

    private RejectHandler rejectHandler;

    @Override
    public Object get(String key) {
        return memCacheClient.get(key);
    }

    @Override
    public boolean insert(String key, Object value) {
        return memCacheClient.insert(key, value);
    }

    @Override
    public boolean update(String key, Object value) {
        return memCacheClient.update(key, value);
    }

    @Override
    public boolean delete(String key) {
        return memCacheClient.delete(key);
    }

    @Override
    public boolean setExpireTime(String key, Long mill) {
        return false;
    }

    @Override
    public void setClient(Object client) {};

    public CacheStrategyEnum getCacheStrategy() {
        return cacheStrategy;
    }

    public void setCacheStrategy(CacheStrategyEnum cacheStrategy) {
        this.cacheStrategy = cacheStrategy;
    }

    public Long getMemSize() {
        return memSize;
    }

    public void setMemSize(Long memSize) {
        this.memSize = memSize;
    }

    public CacheRejectStrategyEnum getCacheRejectStrategy() {
        return cacheRejectStrategy;
    }

    public void setCacheRejectStrategy(CacheRejectStrategyEnum cacheRejectStrategy) {
        this.cacheRejectStrategy = cacheRejectStrategy;
    }

    /**
     * 根据指定配置初始化内存缓存
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        /**
         * 初始化CacheClient
         */
        Constructor constructor;

        switch (cacheStrategy) {
            case FIFO:
                constructor = CacheStrategyEnum.FIFO.getClazz().getConstructor(Integer.class, Long.class);
                memCacheClient = (CacheClient) constructor.newInstance(maxCapacity, memSize);
                break;
            case LFU:
                constructor = CacheStrategyEnum.LFU.getClazz().getConstructor(Integer.class, Long.class);
                memCacheClient = (CacheClient) constructor.newInstance(maxCapacity, memSize);
                break;
            case LRU:
                constructor = CacheStrategyEnum.LRU.getClazz().getConstructor(Integer.class, Long.class);
                memCacheClient = (CacheClient) constructor.newInstance(maxCapacity, memSize);
                break;
            default:
                break;
        }

        /**
         * 初始化RejectHandler
         */
        switch (cacheRejectStrategy) {
            case DISCARD:
                rejectHandler = (RejectHandler) CacheRejectStrategyEnum.DISCARD.getClazz().newInstance();
                // 关联CacheClient与RejectHandler
                rejectHandler.setCacheClient(memCacheClient);
                break;
            case CLEAN:
                rejectHandler = (RejectHandler) CacheRejectStrategyEnum.CLEAN.getClazz().newInstance();
                // 关联CacheClient与RejectHandler
                rejectHandler.setCacheClient(memCacheClient);
                break;
            case TS:
                rejectHandler = (RejectHandler) CacheRejectStrategyEnum.TS.getClazz().newInstance();
                // 关联CacheClient与RejectHandler
                rejectHandler.setCacheClient(memCacheClient);
                break;
            default:
                break;
        }
    }
}