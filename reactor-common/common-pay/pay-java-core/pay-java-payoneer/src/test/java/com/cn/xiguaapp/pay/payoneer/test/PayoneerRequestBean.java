package com.cn.xiguaapp.pay.payoneer.test;

import com.alibaba.fastjson.annotation.JSONField;
import com.cn.xiguaapp.xiguaapp.java.common.core.base.CurType;

import static com.cn.xiguaapp.xiguaapp.java.common.core.base.DefaultCurType.USD;

/**
 * @author xiguaapp
 * @desc
 * @since 1.0 21:17
 */
public class PayoneerRequestBean {
    public PayoneerRequestBean() {

    }

    public PayoneerRequestBean(String payeeId) {
        this.payeeId = payeeId;
    }
    public PayoneerRequestBean(String payeeId, String amount, String clientReferenceId, CurType currency, String description) {
        this.payeeId = payeeId;
        this.amount = amount;
        this.clientReferenceId = clientReferenceId;
        if (null == currency){
            currency = USD;
        }
        this.currency = currency;
        this.description = description;
    }

    /**
     * 收款id
     */
    @JSONField(name="payee_id")
    private String payeeId;
    /**
     * 收款金额
     */
    private String amount;
    /**
     * 订单id
     */
    @JSONField(name="client_reference_id")
    private String clientReferenceId;
    /**
     * 币种
     */
    private CurType currency;
    /**
     * 订单详情 (选填)
     */
    private String description;

    public String getPayeeId() {
        return payeeId;
    }

    public void setPayeeId(String payeeId) {
        this.payeeId = payeeId;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getClientReferenceId() {
        return clientReferenceId;
    }

    public void setClientReferenceId(String clientReferenceId) {
        this.clientReferenceId = clientReferenceId;
    }

    public CurType getCurrency() {
        return currency;
    }

    public void setCurrency(CurType currency) {
        this.currency = currency;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
