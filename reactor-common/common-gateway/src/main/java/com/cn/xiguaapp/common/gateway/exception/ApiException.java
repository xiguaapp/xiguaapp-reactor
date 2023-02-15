package com.cn.xiguaapp.common.gateway.exception;

import com.cn.xiguaapp.common.gateway.message.ErrorFactory;
import com.cn.xiguaapp.common.gateway.message.ErrorMeta;

import java.util.Locale;
import com.cn.xiguaapp.common.gateway.message.Error;

/**
 * @author xiguaapp
 * @Date 2020/10/12
 * @desc
 */
public class ApiException extends RuntimeException {

    private transient Error error;

    private transient ErrorMeta errorMeta;
    private transient Object[] params;

    public ApiException(ErrorMeta errorMeta, Object... params) {
        this.errorMeta = errorMeta;
        this.params = params;
    }

    public ApiException(Throwable cause, ErrorMeta errorMeta, Object... params) {
        super(cause);
        this.errorMeta = errorMeta;
        this.params = params;
    }

    public Error getError(Locale locale) {
        if (error == null) {
            error = ErrorFactory.getError(this.errorMeta, locale, params);
        }
        return error;
    }

    @Override
    public String getMessage() {
        String message = super.getMessage();
        return message == null ? errorMeta.toString() : message;
    }
}
