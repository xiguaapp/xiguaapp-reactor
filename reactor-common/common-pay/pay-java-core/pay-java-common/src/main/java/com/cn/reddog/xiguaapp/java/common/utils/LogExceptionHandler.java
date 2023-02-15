package com.cn.xiguaapp.xiguaapp.java.common.utils;

import com.cn.xiguaapp.xiguaapp.java.common.api.PayErrorExceptionHandler;
import com.cn.xiguaapp.xiguaapp.java.common.exception.PayErrorException;
import lombok.extern.slf4j.Slf4j;

/**
 * @author xiguaapp
 * @desc LogExceptionHandler 日志处理器
 * @since 1.0 17:24
 */
@Slf4j
public class LogExceptionHandler implements PayErrorExceptionHandler {


    @Override
    public void handle(PayErrorException e) {

        log.error("Error happens", e);

    }
}
