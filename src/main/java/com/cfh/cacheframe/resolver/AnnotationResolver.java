package com.cfh.cacheframe.resolver;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * @Author: chenfeihao@corp.netease.com
 * @Date: 2018/11/28
 * 处理自定义注解的接口
 */
public interface AnnotationResolver {
    /**
     * 处理逻辑
     * @param annotation 需要处理的注解
     * @param method 被注解的方法
     * @param target 被代理的对象
     * @param paramMap 方法参数
     */
    public void resolver(Annotation annotation, Method method, Object target, Map<String, Object> paramMap);
}
