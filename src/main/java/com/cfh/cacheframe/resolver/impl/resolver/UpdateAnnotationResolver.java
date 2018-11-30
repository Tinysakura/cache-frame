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
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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

        // 执行update方法对应的方法，获取新的缓存值
        Class clazz = target.getClass();
        String cacheSetMethodName = ((Update) annotation).methodName();
        List<Class<?>> parameterTypes = paramMap.values().stream().map(Object::getClass).collect(Collectors.toList());

        Method cacheSetMethod = null;

        try {
            cacheSetMethod = clazz.getMethod(cacheSetMethodName, parameterTypes.toArray(new Class[]{}));
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        /**
         * 没有获取到对应的缓存设置方法的情况, 放弃更新缓存
         */
        if (cacheSetMethod == null) {
            return;
        }

        // TODO invoke缓存设置方法时如何正确的获取到参数
        Parameter[] cacheSetParameters = cacheSetMethod.getParameters();
        List<Object> parameterValue = Arrays.asList(cacheSetParameters).stream().map(Parameter::getName).map(paramName -> paramMap.get(paramName)).collect(Collectors.toList());

        Object newValue = null;

        try {
            newValue = cacheSetMethod.invoke(target, parameterValue.toArray(new Object[]{}));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        // 如果获取缓存新值的方法执行失败则不更新缓存
        if (newValue == null) {
            return;
        }

        // 更新缓存
        cacheAdaptor.update(key, newValue);

        // TODO 更新二级缓存
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}