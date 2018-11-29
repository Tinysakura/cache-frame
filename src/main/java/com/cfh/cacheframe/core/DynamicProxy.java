package com.cfh.cacheframe.core;

import com.cfh.cacheframe.adapter.CacheAdaptor;
import com.cfh.cacheframe.annotation.Cache;
import com.cfh.cacheframe.resolver.impl.resolver.CacheAnnotationResolver;
import com.cfh.cacheframe.util.ReflectionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.concurrent.Semaphore;

/**
 * @Author: chenfeihao@corp.netease.com
 * @Date: 2018/11/29
 * 缓存逻辑的动态代理
 */
public class DynamicProxy implements InvocationHandler, ApplicationContextAware, InitializingBean {
    private Logger log = LoggerFactory.getLogger(DynamicProxy.class);

    private Object target;

    private ApplicationContext applicationContext;

    private CacheAdaptor cacheAdaptor;

    private Semaphore semaphore;

    /**
     * 获取配置文件中是否打开二级缓存的配置
     */
    @Value("${secondary_cache_enable}")
    private boolean secondaryCacheEnable;

    /**
     * 当缓存失效或尚未设置的情况下允许同时直接访问数据库的请求数（解决缓存并发问题）
     */
    @Value("${direct_read_concurrent_num}")
    private int directReadConcurrentNum;

    public DynamicProxy(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 判断当前方法是否需要被代理
        Annotation cacheAnnotation = method.getAnnotation(Cache.class);

        // 如果没有被@Cache注解标注则使用原本的逻辑
        if (cacheAnnotation == null) {
            return method.invoke(target, args);
        }

        String key = ReflectionUtil.key(cacheAnnotation, method, ReflectionUtil.getParamMap(method, args), applicationContext, log);
        // 尝试从缓存中获取
        Object result = cacheAdaptor.get(key);

        // 成功获取缓存
        if (result != null) {
            return result;
        }

        // 获取缓存失败，尝试从内存缓存获取
        if (secondaryCacheEnable) {
            // TODO 从内存获取缓存内容的相关逻辑
        }

        // 若二级缓存也获取失败，则直接访问数据库然后重新设置缓存，使用信号量机制防止缓存并发问题
        semaphore.acquire();

        try {
            Object fromDB = method.invoke(target,args);
            cacheAdaptor.insert(key, fromDB);

            return fromDB;
        } finally {
            semaphore.release();
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
        if (applicationContext != null) {
            cacheAdaptor = applicationContext.getBean(CacheAdaptor.class);
        }
    }

    @Override
    public void afterPropertiesSet() {
        // 初始化计数信号量
        this.semaphore = new Semaphore(directReadConcurrentNum);
    }
}