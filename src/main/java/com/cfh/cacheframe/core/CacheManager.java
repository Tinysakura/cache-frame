package com.cfh.cacheframe.core;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: chenfeihao@corp.netease.com
 * @Date: 2018/11/29
 * 缓存管理器
 */
@Component
public class CacheManager implements InitializingBean {
    private ConcurrentHashMap<String, Object> dynamicInstanceMap;

    /**
     * 保存代理对象
     * @param object
     */
    public void addDynamicInstance(Object object) {
        dynamicInstanceMap.put(object.getClass().getName(), object);
    }

    /**
     * 获取代理对象
     * @param fullClassName
     */
    public Object getDynamicInstance(String fullClassName) {
        return dynamicInstanceMap.get(fullClassName);
    }

    public boolean dynamicInstanceContain(String fullClassName) {
        return dynamicInstanceMap.containsKey(fullClassName);
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        dynamicInstanceMap = new ConcurrentHashMap<>();
    }
}