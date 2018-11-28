package com.cfh.cacheframe.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author: chenfeihao@corp.netease.com
 * @Date: 2018/11/28
 * 方法字段注解，被该注解标注的方法字段会作为key generate的参数
 */
@Target({ElementType.PARAMETER})// 注解的作用域为方法字段
@Retention(RetentionPolicy.RUNTIME)
public @interface KeyParam {

}
