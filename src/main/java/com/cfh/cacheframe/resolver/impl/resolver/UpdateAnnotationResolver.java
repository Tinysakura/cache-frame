package com.cfh.cacheframe.resolver.impl.resolver;

import com.cfh.cacheframe.adapter.CacheAdaptor;
import com.cfh.cacheframe.annotation.Update;
import com.cfh.cacheframe.resolver.AnnotationResolver;
import com.cfh.cacheframe.util.ReflectionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * @Author: chenfeihao@corp.netease.com
 * @Date: 2018/11/28
 * @Update 注解处理器
 */
@Component
public class UpdateAnnotationResolver implements AnnotationResolver, ApplicationContextAware {
    private Logger log = LoggerFactory.getLogger(DeleteAnnotationResolver.class);
    private ApplicationContext applicationContext;

    @Autowired
    private CacheAdaptor cacheAdaptor;

    @Override
    public void resolver(Annotation annotation, Method method, Object target, Map<String, Object> paramMap) {
        if (!(annotation instanceof Update)) {
            return;
        }

        String key = ReflectionUtil.key(annotation, method, paramMap, applicationContext, log);
        cacheAdaptor.delete(key);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}