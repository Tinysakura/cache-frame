package com.cfh.cacheframe.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author: chenfeihao@corp.netease.com
 * @Date: 2018/11/28
 * 被该注解注解的方法在调用完成后会根据缓存的key更新缓存
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface Update {
    // 需要被更新的缓存的key
    String key() default "";

    // key生成策略的全类名
    String keyGenetator() default "";

    // 更新后的缓存的失效时间(ms)
    long expireTime() default Long.MAX_VALUE;

    // 所要更新的缓存对应的设置缓存方法的方法名（默认两个方法在同一个类中）
    String methodName();
}
