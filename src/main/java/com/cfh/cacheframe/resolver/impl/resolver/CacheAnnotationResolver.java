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
        if (!(annotation instanceof Cache)) {
            return;
        }

        String effectiveKey = key(annotation, method, paramMap);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * 生成缓存key
     * @param annotation
     * @param method
     * @param paramMap
     * @return
     */
    private String key(Annotation annotation, Method method, Map<String, Object> paramMap) {
        String effectiveKey;

        // 获得缓存key
        String key = ((Cache) annotation).key();
        String keyGenerator = ((Cache) annotation).keyGenerator();

        // 如果这两个注解的字段都为空则使用默认的生成规则
        if (StringUtil.isEmpty(key) && StringUtil.isEmpty(keyGenerator)) {
            effectiveKey = applicationContext.getBean(DefaultkeyGenerator.class).generateKey(method, paramMap);

            return effectiveKey;
        }

        // key属性不为空且keyGenerator属性为空的情况
        if (!StringUtil.isEmpty(key) && StringUtil.isEmpty(keyGenerator)) {
            effectiveKey = key;
            return effectiveKey;
        }

        // keyGenerator属性不为空的情况
        if (!StringUtil.isEmpty(keyGenerator)) {
            Class clazz = null;
            try {
                clazz = Class.forName(keyGenerator).getClass();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            // 如果出现ClassNotFoundException异常则继续使用默认的key生成策略
            if (clazz == null) {
                log.info("缓存key生成策略{}不存在，使用默认的生成策略");
                effectiveKey = applicationContext.getBean(DefaultkeyGenerator.class).generateKey(method, paramMap);
                return effectiveKey;
            }

            KeyGenerator customeKeyGenerator = (KeyGenerator)applicationContext.getBean(clazz);
            if (customeKeyGenerator == null) {
                log.info("缓存key生成策略{}没有注入容器，使用默认的生成策略");
                effectiveKey = applicationContext.getBean(DefaultkeyGenerator.class).generateKey(method, paramMap);
                return effectiveKey;
            }

            return (String) customeKeyGenerator.generateKey(method, paramMap);
        }

        return applicationContext.getBean(DefaultkeyGenerator.class).generateKey(method, paramMap);
    }
}