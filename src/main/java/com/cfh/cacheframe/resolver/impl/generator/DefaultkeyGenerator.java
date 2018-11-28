package com.cfh.cacheframe.resolver.impl.generator;

import com.cfh.cacheframe.annotation.KeyParam;
import com.cfh.cacheframe.resolver.KeyGenerator;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @Author: chenfeihao@corp.netease.com
 * @Date: 2018/11/28
 * 默认的缓存key的生成规则
 */
@Component
public class DefaultkeyGenerator implements KeyGenerator<String> {

    /**
     * 默认的key为方法名的内容提取再加上以'-'分隔符隔开的参数组成
     * @param method 需要生成缓存的方法
     * @param paramMap 方法参数
     * @return
     */
    @Override
    public String generateKey(Method method, Map<String, Object> paramMap) {
        String methodName = method.getName();

        methodName = handleQueryWord(methodName);
        methodName = handleConditionWord(methodName);

        // 方法中只有被@KeyParam注解的字段才会作为key生成的条件
        Parameter[] parameters = method.getParameters();
        List<Object> keyParams = new ArrayList<>();

        Arrays.asList(parameters).forEach(e -> {
            if (e.getAnnotation(KeyParam.class) != null) {
                keyParams.add(paramMap.get(e.getName()));
            }
        });

        String generatorKey = methodNameLinkParams(methodName, keyParams.toArray(new Object[]{}));

        return generatorKey;
    }

    /**
     * 处理方法名中的查询字段
     * @param methodName
     * @return
     */
    private String handleQueryWord(String methodName) {
        String[] queryWords = {"get", "find", "query", "search", "obtain", "gain", "acquire"};

        for (String queryWord : queryWords) {
            if (methodName.startsWith(queryWord)) {
                return methodName.substring(queryWord.length(), methodName.length());
            }
        }

        return methodName;
    }

    /**
     * 处理方法中的条件字段
     * @param methodName
     * @return
     */
    private String handleConditionWord(String methodName) {
        String[] conditionWords = {"in", "by", "at", "equal", "notEqual", "surpass", "under", "below", "greater", "than", "over"};

        for (String conditionWord : conditionWords) {
            if (methodName.contains(conditionWord)) {
                String[] splitResult = methodName.split(conditionWord);

                // 非条件字段中不包含条件字段内容的情况
                if (splitResult.length == 1) {
                    return splitResult[0];
                }

                // 非条件字段中包含条件字段内容的情况
                if (splitResult.length > 2) {
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < splitResult.length-1; i++) {
                        sb.append(splitResult[i]).append(conditionWord);
                    }

                    return sb.toString().substring(0, sb.length() - conditionWord.length());
                }
            }
        }

        return methodName;
    }

    /**
     * 连接处理过后的方法名与被@KeyParam标注的参数值
     * @param methodName
     * @param params
     * @return
     */
    private String methodNameLinkParams(String methodName, Object[] params) {
        StringBuilder sb = new StringBuilder();

        sb.append(methodName);

        Arrays.asList(params).forEach(e -> sb.append("_").append(e.toString()));

        return sb.toString();
    }
}