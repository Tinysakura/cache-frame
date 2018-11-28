package com.cfh.cacheframe.resolver;

import java.lang.reflect.Method;

/**
 * @Author: chenfeihao@corp.netease.com
 * @Date: 2018/11/28
 * 降级处理逻辑接口
 */

public interface FallbackStrategy {
    public void fallback(Method method);
}