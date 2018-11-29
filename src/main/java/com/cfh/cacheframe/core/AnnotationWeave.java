package com.cfh.cacheframe.core;

import com.cfh.cacheframe.annotation.Cache;
import com.cfh.cacheframe.annotation.Delete;
import com.cfh.cacheframe.annotation.Update;
import com.cfh.cacheframe.resolver.impl.resolver.CacheAnnotationResolver;
import com.cfh.cacheframe.resolver.impl.resolver.DeleteAnnotationResolver;
import com.cfh.cacheframe.resolver.impl.resolver.UpdateAnnotationResolver;
import com.cfh.cacheframe.util.ReflectionUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

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

    @Autowired
    private CacheManager cacheManager;

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
    public Object handleCacheAnnotation(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        /**
         * 生成动态代理
         */
        if (!cacheManager.dynamicInstanceContain(joinPoint.getTarget().getClass().getName())) {
            DynamicProxy dynamicProxy = new DynamicProxy(joinPoint.getTarget());
            // 获取service接口(默认service层的实现类只实现了对于的service接口)
            Class interfaze = joinPoint.getTarget().getClass().getInterfaces()[0];
            // 生成动态代理对象
            Object dynamicInstance = Proxy.newProxyInstance(interfaze.getClassLoader(), new Class[]{interfaze}, dynamicProxy);
            // 将动态代理对象交由CacheManager管理
            cacheManager.addDynamicInstance(dynamicInstance);
        }

        Object dynamicInstance = cacheManager.getDynamicInstance(joinPoint.getTarget().getClass().getName());

        // 使用代理的逻辑替换service层原先的逻辑
        try {
            return method.invoke(dynamicInstance, joinPoint.getArgs());
        } catch (Exception e) {
            e.printStackTrace();
            return e;
        }
    }

    @Around(value = "deletePointcut()")
    public void handleDeleteAnnotation(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        Annotation cacheAnnotation = method.getAnnotation(Delete.class);
        try {
            joinPoint.proceed(joinPoint.getArgs());
            deleteAnnotationResolver.resolver(cacheAnnotation, method, joinPoint.getTarget(), ReflectionUtil.getParamMap(method, joinPoint.getArgs()));
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    @Around(value = "updatePointcut()")
    public void handleUpdateAnnotation(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        Annotation cacheAnnotation = method.getAnnotation(Update.class);
        try {
            joinPoint.proceed(joinPoint.getArgs());
            deleteAnnotationResolver.resolver(cacheAnnotation, method, joinPoint.getTarget(), ReflectionUtil.getParamMap(method, joinPoint.getArgs()));
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }
}