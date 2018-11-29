package com.cfh.cacheframe.util;

import com.cfh.cacheframe.annotation.Cache;
import com.cfh.cacheframe.resolver.KeyGenerator;
import com.cfh.cacheframe.resolver.impl.generator.DefaultkeyGenerator;
import org.slf4j.Logger;
import org.springframework.context.ApplicationContext;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: chenfeihao@corp.netease.com
 * @Date: 2018/11/29
 */

public class ReflectionUtil {

    public static HashMap<String, Object> getParamMap(Method method, Object[] paramValues) {
        HashMap<String, Object> paramMap = new HashMap<>();

        if (paramValues == null || paramValues.length == 0) {
            return paramMap;
        }

        Parameter[] parameters = method.getParameters();

        for (int i = 0; i < parameters.length; i++) {
            paramMap.put(parameters[i].getName(), paramValues[i]);
        }

        return paramMap;
    }

    /**
     * 生成缓存key
     * @param annotation
     * @param method
     * @param paramMap
     * @return
     */
    public static String key(Annotation annotation, Method method, Map<String, Object> paramMap, ApplicationContext applicationContext, Logger log) {
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