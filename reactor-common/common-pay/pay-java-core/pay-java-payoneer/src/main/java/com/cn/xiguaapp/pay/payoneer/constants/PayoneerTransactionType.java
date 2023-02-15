package com.cn.xiguaapp.pay.payoneer.constants;

import com.cn.xiguaapp.xiguaapp.java.common.core.base.TransactionType;

/**
 * @author xiguaapp
 * @desc PayoneerT支付类型
 * @since 1.0 21:12
 */
public enum PayoneerTransactionType implements TransactionType {

    /**
     * 注册授权
     */
    REGISTRATION("payees/registration-link"),
    /**
     * 授权状态
     */
    PAYEES_STATUS("payees/{payee_id}/status"),
    /**
     * 用户信息
     */
    PAYEES_DETAILS("payees/{payee_id}/details"),
    /**
     * 收款
     */
    CHARGE("charges"),
    /**
     * 取消收款(取消订单与退款)
     */
    CHARGE_CANCEL("charges/{client_reference_id}/cancel"),
    /**
     * 查询收款订单与订单状态
     */
    CHARGE_STATUS("charges/{client_reference_id}/status"),
    /**
     * 转账
     */
    PAYOUTS("payouts"),
    /**
     * 转账状态查询
     */
    PAYOUT_STATUS("payouts/{client_reference_id}")
    ;

    private String method;

    PayoneerTransactionType(String method) {
        this.method = method;
    }

    @Override
    public String getType() {
        return this.name();
    }

    /**
     * 获取接口名称
     * @return 接口名称
     */
    @Override
    public String getMethod() {
        return this.method;
    }
}