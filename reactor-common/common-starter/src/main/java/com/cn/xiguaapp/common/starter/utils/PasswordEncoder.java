package com.cn.xiguaapp.common.starter.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @author xiguaapp
 * @Date 2020/7/28
 * @desc
 */
public class PasswordEncoder {
    /**
     * 小小加密
     * @param str
     * @return
     */
    public static String encoder(String str){
        return new BCryptPasswordEncoder().encode(str);
    }

    /**
     * 判断两次结果是否相等
     * @param password 未经过加密的
     * @param encoderPassword 已经进行加密的
     * @return 结果是否相同
     */
    public static boolean match(String password,String encoderPassword){
        return new BCryptPasswordEncoder().matches(password,encoderPassword);
    }
}
