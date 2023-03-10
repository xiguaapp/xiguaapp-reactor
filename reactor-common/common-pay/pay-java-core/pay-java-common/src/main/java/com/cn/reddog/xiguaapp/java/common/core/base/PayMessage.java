package com.cn.xiguaapp.xiguaapp.java.common.core.base;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Map;

/**
 * @author xiguaapp
 * @desc 支付回调消息
 *  基础实现，具体可根据具体支付回调的消息去实现
 * @since 1.0 18:40
 */
public class PayMessage implements Serializable {
    private Map<String, Object> payMessage = null;
    private String payType;
    private String transactionType;
    private String fromPay;
    private String describe;


    public PayMessage() {
    }

    public PayMessage(Map<String, Object> payMessage) {
        this.payMessage = payMessage;
    }

    public PayMessage(Map<String, Object> payMessage, String payType) {
        this.payMessage = payMessage;
        this.payType = payType;
    }


    public PayMessage(Map<String, Object> payMessage, String payType, String transactionType) {
        this.payMessage = payMessage;
        this.payType = payType;
        this.transactionType = transactionType;
    }

    protected void setPayMessage(Map<String, Object> payMessage) {
        this.payMessage = payMessage;
    }


    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getFromPay() {
        return fromPay;
    }

    public void setFromPay(String fromPay) {
        this.fromPay = fromPay;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getDiscount() {
        return (String) payMessage.get("discount");
    }

    public String getSubject() {
        return (String) payMessage.get("subject");
    }


    /////////微信与支付宝共用
    public String getOutTradeNo() {
        return (String) payMessage.get("out_trade_no");
    }

    public String getSign() {
        return (String) payMessage.get("sign");
    }

    public Number getTotalFee() {
        String totalFee = (String) payMessage.get("total_fee");
        if (null == totalFee || "".equals(totalFee)) {
            return 0;
        }
        if (isNumber(totalFee)) {
            return new BigDecimal(totalFee);
        }
        return 0;
    }

    /////////微信与支付宝共用


    public boolean isNumber(String str) {
        return str.matches("^(-?[1-9]\\d*\\.?\\d*)|(-?0\\.\\d*[1-9])|(-?[0])|(-?[0]\\.\\d*)$");
    }


    @Override
    public String toString() {
        return payMessage.toString();
    }

    public Map<String, Object> getPayMessage() {
        return payMessage;
    }

}
