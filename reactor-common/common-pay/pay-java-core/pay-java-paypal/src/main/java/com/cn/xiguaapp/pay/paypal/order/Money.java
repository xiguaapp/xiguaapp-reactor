package com.cn.xiguaapp.pay.paypal.order;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * @author xiguaapp
 * @desc
 * @since 1.0 21:26
 */
public class Money {
    @JSONField(name = "currency_code")
    private String currencyCode;
    @JSONField(name = "value")
    private String value;

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Money currencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
        return this;
    }


    public Money value(String value) {
        this.value = value;
        return this;
    }
}
