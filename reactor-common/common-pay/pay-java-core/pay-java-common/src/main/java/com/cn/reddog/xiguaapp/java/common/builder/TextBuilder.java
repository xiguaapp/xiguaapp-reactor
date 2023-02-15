package com.cn.xiguaapp.xiguaapp.java.common.builder;

import com.cn.xiguaapp.xiguaapp.java.common.core.PayOutMessage;
import com.cn.xiguaapp.xiguaapp.java.common.core.PayTextOutMessage;

/**
 * @author xiguaapp
 * @desc 支付回调文本组装类
 * @since 1.0 14:12
 */
public class TextBuilder extends BaseBuilder<TextBuilder, PayOutMessage> {
    private String content;

    public TextBuilder content(String content) {
        this.content = content;
        return this;
    }

    @Override
    public PayOutMessage build() {
        PayTextOutMessage message = new PayTextOutMessage();
        setCommon(message);
        message.setContent(content);
        return message;
    }
}
