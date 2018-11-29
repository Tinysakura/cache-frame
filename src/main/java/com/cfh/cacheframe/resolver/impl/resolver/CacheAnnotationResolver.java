package com.cfh.cacheframe.resolver.impl.resolver;

import com.cfh.cacheframe.annotation.Cache;
import com.cfh.cacheframe.resolver.AnnotationResolver;
import com.cfh.cacheframe.resolver.KeyGenerator;
import com.cfh.cacheframe.resolver.impl.generator.DefaultkeyGenerator;
import com.cfh.cacheframe.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * @Author: chenfeihao@corp.netease.com
 * @Date: 2018/11/28
 * @Cache 注解处理器
 */
@Component
public class CacheAnnotationResolver implements AnnotationResolver, ApplicationContextAware {
    private static final Logger log = LoggerFactory.getLogger(CacheAnnotationResolver.class);
    private ApplicationContext applicationContext;

    @Override
    public void resolver(Annotation annotation, Method method, Object target, Map<String, Object> paramMap) {
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}