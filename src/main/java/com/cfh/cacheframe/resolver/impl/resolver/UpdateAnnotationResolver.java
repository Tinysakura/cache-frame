package com.cfh.cacheframe.resolver.impl.resolver;

import com.cfh.cacheframe.resolver.AnnotationResolver;
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
public class UpdateAnnotationResolver implements AnnotationResolver {
    @Override
    public void resolver(Annotation annotation, Method method, Object target, Map<String, Object> paramMap) {

    }
}