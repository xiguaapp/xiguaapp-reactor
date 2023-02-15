package com.cn.xiguaapp.xiguaapp.java.common.builder;

import com.cn.xiguaapp.xiguaapp.java.common.core.PayOutMessage;
import com.cn.xiguaapp.xiguaapp.java.common.core.PayXmlOutMessage;

/**
 * @author xiguaapp
 * @desc 支付回调xml组装类
 * @since 1.0 14:13
 */
public class XmlBuilder extends BaseBuilder<XmlBuilder, PayOutMessage> {
    private String content;
    private String code;
    public XmlBuilder content(String content) {
        this.content = content;
        return this;
    }

    public XmlBuilder code(String code) {
        this.code = code;
        return this;
    }


    @Override
    public PayOutMessage build() {
        PayXmlOutMessage message = new PayXmlOutMessage();
        setCommon(message);
        message.setContent(content);
        message.setCode(code);
        return message;
    }
}
