package com.cn.xiguaapp.xiguaapp.java.common.core;

import com.alibaba.fastjson.JSONObject;
import com.cn.xiguaapp.xiguaapp.java.common.builder.XmlBuilder;
import com.cn.xiguaapp.xiguaapp.java.common.builder.JsonBuilder;
import com.cn.xiguaapp.xiguaapp.java.common.builder.TextBuilder;

import java.io.Serializable;

/**
 * @author xiguaapp
 * @desc 支付回调返回信息
 * @since 1.0 14:06
 */
public abstract class PayOutMessage implements Serializable {
    protected String content;


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    /**
     * 获得文本消息builder
     *
     * @return 文本消息builder
     */
    public static TextBuilder TEXT() {
        return new TextBuilder();
    }

    /**
     * 获得XML消息builder
     *
     * @return XML消息builder
     */
    public static XmlBuilder XML() {
        return new XmlBuilder();
    }

    /**
     * 获得Json消息builder
     *
     * @return Json消息builder
     */
    public static JsonBuilder JSON() {
        return new JsonBuilder(new JSONObject());
    }

    public abstract String toMessage();
}
