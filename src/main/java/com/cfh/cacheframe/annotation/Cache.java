package com.cfh.cacheframe.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author: chenfeihao@corp.netease.com
 * @Date: 2018/11/28
 * 使用缓存注解，当注解在单个方法上时，方法的返回值会作为value缓存，默认key为方法名与参数的组合
 * 当注解在类上时，该类所有的方法的返回值都会被缓存
 */
@Target({ElementType.METHOD})// 注解的作用域为方法
@Retention(RetentionPolicy.RUNTIME)
public @interface Cache {
    // 设置缓存的key
    String key() default "";

    // 设置缓存的key的生成策略的全类名，优先级高于key
    String keyGenerator() default "";

    // 是否开启二级缓存,默认不打开
    boolean openStandbyCache() default false;

    // 缓存失效时间(ms)
    long expireTime() default Long.MAX_VALUE;

    // 缓存降级策略的全类名
    String fallBack() default "";

    // 熔断表达式 格式@{failure rate(int):period of time(ms)} 即一段时间内的失败比率
    String fusingExpression() default "50:100";

    // 半开状态下可以接受的最大请求数
    long maxReciveRequest() default 100L;

    // 是否打开缓存自动更新,默认不打开
    boolean smartAutoRefresh() default false;
}

