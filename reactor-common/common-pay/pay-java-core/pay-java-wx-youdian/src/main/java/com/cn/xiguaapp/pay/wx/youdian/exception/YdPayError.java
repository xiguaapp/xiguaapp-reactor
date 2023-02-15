package com.cn.xiguaapp.pay.wx.youdian.exception;

import com.cn.xiguaapp.xiguaapp.java.common.result.PayError;

/**
 * @author xiguaapp
 * @desc 友店支付异常类
 * @since 1.0 22:16
 */
public class YdPayError implements PayError {

    private int errorcode;
    private String msg;
    private String content;

    @Override
    public String getErrorCode() {
        return errorcode + "";
    }

    @Override
    public String getErrorMsg() {
        return msg;
    }

    public YdPayError(int errorcode, String msg) {
        this.errorcode = errorcode;
        this.msg = msg;
    }

    public YdPayError(int errorcode, String msg, String content) {
        this.errorcode = errorcode;
        this.msg = msg;
        this.content = content;
    }

    @Override
    public String getString() {
        return "支付错误: errcode=" + errorcode + ", msg=" + msg + (null == content ? "" : "\n content:" + content);
    }
}
