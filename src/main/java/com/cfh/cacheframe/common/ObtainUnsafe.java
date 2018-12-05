package com.cfh.cacheframe.common;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import sun.misc.Unsafe;

/**
 * @Author: chenfeihao@corp.netease.com
 * @Date: 2018/12/5
 * 通过反射获取Unsafe对象
 */
@Component
public class ObtainUnsafe implements InitializingBean {

    private Unsafe UNSAFE;

    @Override
    public void afterPropertiesSet() throws Exception {
        UNSAFE = (Unsafe) Unsafe.class.getDeclaredField("theUnsafe").get(null);
    }

    public Unsafe getUNSAFE() {
        return UNSAFE;
    }

    public void setUNSAFE(Unsafe UNSAFE) {
        this.UNSAFE = UNSAFE;
    }
}