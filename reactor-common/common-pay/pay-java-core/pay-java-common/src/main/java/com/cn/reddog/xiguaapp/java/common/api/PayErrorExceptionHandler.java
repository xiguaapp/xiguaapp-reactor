package com.cn.xiguaapp.xiguaapp.java.common.api;

import com.cn.xiguaapp.xiguaapp.java.common.exception.PayErrorException;

/**
 * @author xiguaapp
 * @desc PayErrorExceptionHandler处理器
 * @since 1.0 17:23
 */
public interface PayErrorExceptionHandler {
    /**
     * 异常统一处理器
     * @param e 支付异常
     */
    void handle(PayErrorException e);
}
