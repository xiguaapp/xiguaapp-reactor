package com.cn.xiguaapp.xiguaapp.java.common.core;

/**
 * @author xiguaapp
 * @desc 支付回调返回文本信息
 * @since 1.0 14:10
 */
public class PayTextOutMessage extends PayOutMessage{

    public PayTextOutMessage() {
    }

    @Override
    public String toMessage() {
        return getContent();
    }
}