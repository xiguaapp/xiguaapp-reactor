package com.cn.xiguaapp.thrid.auth.config;

import com.cn.xiguaapp.thrid.auth.log.Log;

/**
 * @author xiguaapp
 * @desc 日志配置类
 * @since 1.0 21:51
 */
public class JustAuthLogConfig {
    /**
     * 设置日志级别
     * @param level 日志级别
     */
    public static void setLevel(Log.Level level){
        Log.Config.level = level;
    }

    /**
     * 关闭日志
     */
    public static void disable(){
        Log.Config.enable = false;
    }

    /**
     * 开启日志
     */
    public static void enable(){
        Log.Config.enable = true;
    }
}
