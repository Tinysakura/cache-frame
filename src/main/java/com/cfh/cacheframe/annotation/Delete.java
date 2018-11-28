package com.cfh.cacheframe.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author: chenfeihao@corp.netease.com
 * @Date: 2018/11/28
 * 被该注解注解的方法在执行完之后将会删除key对应的缓存
 */
@Target({ElementType.METHOD})// 注解的作用域为方法
@Retention(RetentionPolicy.RUNTIME)
public @interface Delete {
    String key() default "";

    String keyGenetator() default "";
}
