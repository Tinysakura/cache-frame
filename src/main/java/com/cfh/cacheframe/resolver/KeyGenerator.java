package com.cfh.cacheframe.resolver;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * @Author: chenfeihao@corp.netease.com
 * @Date: 2018/11/28
 * 定义key的生成规则的接口
 */

public interface KeyGenerator<T>{

    public T generateKey(Method method, Map<String, Object> paramMap);
}