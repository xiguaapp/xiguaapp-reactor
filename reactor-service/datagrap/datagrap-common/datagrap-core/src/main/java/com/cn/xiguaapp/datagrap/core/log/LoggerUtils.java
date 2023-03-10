package com.cn.xiguaapp.datagrap.core.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author xiguaapp
 * @data 2021-02-21
 * @desc 日志工具类
 */
public class LoggerUtils {
    public static <T> Logger Logger(Class<T> clazz) {
        return LoggerFactory.getLogger(clazz);
    }

    /**
     * 打印到指定的文件下
     *
     * @param logService 日志文件名称
     * @return
     */
    public static Logger Logger(LogService logService) {
        return LoggerFactory.getLogger(logService.getLoggerName());
    }
}
