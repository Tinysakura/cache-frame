package com.cfh.cacheframe.core;

import com.cfh.cacheframe.annotation.Cache;
import com.cfh.cacheframe.annotation.Delete;
import com.cfh.cacheframe.annotation.Update;
import com.cfh.cacheframe.resolver.impl.resolver.CacheAnnotationResolver;
import com.cfh.cacheframe.resolver.impl.resolver.DeleteAnnotationResolver;
import com.cfh.cacheframe.resolver.impl.resolver.UpdateAnnotationResolver;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

/**
 * @Author: chenfeihao@corp.netease.com
 * @Date: 2018/11/28
 * 使用Aop织入@Cache, @Delete和@Update三个注解的注解处理逻辑
 */
@Component
public class AnnotationWeave {
    @Autowired
    private CacheAnnotationResolver cacheAnnotationResolver;

    @Autowired
    private DeleteAnnotationResolver deleteAnnotationResolver;

    @Autowired
    private UpdateAnnotationResolver updateAnnotationResolver;

    /**
     * @Cache注解的切入点
     */
    @Pointcut(value = "@annotation(com.cfh.cacheframe.annotation.Cache)")
    public void cachePointcut(){}

    /**
     * @Delete注解的切入点
     */
    @Pointcut(value = "@annotation(com.cfh.cacheframe.annotation.Delete)")
    public void deletePointcut(){}

    /**
     * @Update注解的切入点
     */
    @Pointcut(value = "@annotation(com.cfh.cacheframe.annotation.Update)")
    public void updatePointcut(){}

    @Around(value = "cachePointcut()")
    public void handleCacheAnnotation(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        Annotation cacheAnnotation = method.getAnnotation(Cache.class);
        cacheAnnotationResolver.resolver(cacheAnnotation, method, joinPoint.getTarget(), getParamMap(method, joinPoint.getArgs()));
    }

    @Around(value = "deletePointcut()")
    public void handleDeleteAnnotation(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        Annotation cacheAnnotation = method.getAnnotation(Delete.class);
        deleteAnnotationResolver.resolver(cacheAnnotation, method, joinPoint.getTarget(), getParamMap(method, joinPoint.getArgs()));
    }

    @Around(value = "updatePointcut()")
    public void handleUpdateAnnotation(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        Annotation cacheAnnotation = method.getAnnotation(Update.class);
        deleteAnnotationResolver.resolver(cacheAnnotation, method, joinPoint.getTarget(), getParamMap(method, joinPoint.getArgs()));
    }

    private HashMap<String, Object> getParamMap(Method method, Object[] paramValues) {
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
}