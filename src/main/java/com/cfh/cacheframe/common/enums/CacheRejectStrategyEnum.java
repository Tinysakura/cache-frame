package com.cfh.cacheframe.common.enums;

import com.cfh.cacheframe.adapter.reject.CleanRejectHandler;
import com.cfh.cacheframe.adapter.reject.DiscardRejectHandler;
import com.cfh.cacheframe.adapter.reject.TSRejectHandler;

/**
 * @Author: chenfeihao@corp.netease.com
 * @Date: 2018/12/5
 */

public enum CacheRejectStrategyEnum {
    /**
     * 直接丢弃
     */
    DISCARD("discard", DiscardRejectHandler.class),
    /**
     * 清理缓存容器释放空间
     */
    CLEAN("clean", CleanRejectHandler.class),
    /**
     * 使用一个有界队列暂存缓存数据等待空闲的缓存空间
     */
    TS("temporaryStorage", TSRejectHandler.class)
    ;

    private String rejectStrategyName;
    private Class clazz;

    CacheRejectStrategyEnum(String rejectStrategyName, Class clazz) {
        this.rejectStrategyName = rejectStrategyName;
        this.clazz = clazz;
    }

    public String getRejectStrategyName() {
        return rejectStrategyName;
    }

    public void setRejectStrategyName(String rejectStrategyName) {
        this.rejectStrategyName = rejectStrategyName;
    }

    public Class getClazz() {
        return clazz;
    }

    public void setClazz(Class clazz) {
        this.clazz = clazz;
    }
}