package com.cfh.cacheframe.util;

/**
 * @Author: chenfeihao@corp.netease.com
 * @Date: 2018/11/28
 * 字符串工具类
 */

public class StringUtil {

    public static boolean isEmpty(String s) {
        return s != null && s.length() > 0 ? false:true;
    }
}