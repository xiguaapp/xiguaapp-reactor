package com.cn.xiguaapp.pay.paypal.api;

import com.cn.xiguaapp.xiguaapp.java.common.builder.TextBuilder;

import java.util.Map;

/**
 * @author xiguaapp
 * @desc
 * @since 1.0 21:31
 */
public class PayPalOutMessageBuilder extends TextBuilder {


    public PayPalOutMessageBuilder(Map<String, Object> message) {
        StringBuilder out = new StringBuilder();
        for (Map.Entry<String, Object> entry : message.entrySet()) {
            out.append(entry.getKey()).append('=').append(entry.getValue()).append("<br>");
        }
        super.content(out.toString());
    }


}