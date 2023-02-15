package com.cn.xiguaapp.thrid.auth.utils;

import com.cn.xiguapp.common.core.utils.SnowflakeIdGenerator;

/**
 * @author xiguaapp
 * @desc AuthState工具类，默认只提供一个创建随机uuid的方法
 * @since 1.0 22:16
 */
public class AuthStateUtils {
    /**
     * 生成随机state，采用https://github.com/lets-mica/mica的UUID工具
     *
     * @return 随机的state字符串
     */
    public static String createState() {
        return SnowflakeIdGenerator.getInstance().toString();
    }
}
