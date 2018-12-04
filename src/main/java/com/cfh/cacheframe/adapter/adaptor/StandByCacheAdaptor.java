package com.cfh.cacheframe.adapter.adaptor;

import com.cfh.cacheframe.adapter.CacheAdaptor;
import com.cfh.cacheframe.adapter.CacheClient;
import com.cfh.cacheframe.common.enums.CacheRejectStrategyEnum;
import com.cfh.cacheframe.common.enums.CacheStrategyEnum;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

/**
 * @Author: chenfeihao@corp.netease.com
 * @Date: 2018/12/4
 * 二级缓存（内存缓存）
 */
@Component
public class StandByCacheAdaptor implements CacheAdaptor, InitializingBean {
    /**
     * 使用的缓存策略
     */
    private CacheStrategyEnum cacheStrategy;

    /**
     * 用于二级缓存的内存(/mb)
     */
    private Long memSize;

    /**
     * 缓存大小超出限制后的丢弃策略
     */
    private CacheRejectStrategyEnum cacheRejectStrategy;

    private CacheClient memCacheClient;

    @Override
    public Object get(String key) {
        return null;
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

    }

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

    }
}