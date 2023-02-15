package com.cn.xiguaapp.xiguaapp.java.common.exception;

import com.cn.xiguaapp.xiguaapp.java.common.result.PayError;

/**
 * @author xiguaapp
 * @desc 支付异常
 * @since 1.0 14:03
 */
public class PayErrorException extends RuntimeException{
    private PayError error;

    public PayErrorException(PayError error) {
        super(error.getString());
        this.error = error;
    }


    public PayError getPayError() {
        return error;
    }
}
