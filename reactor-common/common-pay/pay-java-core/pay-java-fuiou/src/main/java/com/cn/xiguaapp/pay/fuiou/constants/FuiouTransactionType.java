package com.cn.xiguaapp.pay.fuiou.constants;

import com.cn.xiguaapp.xiguaapp.java.common.core.base.TransactionType;

/**
 * @author xiguaapp
 * @desc 支付类型
 * @since 1.0 21:03
 */
public enum FuiouTransactionType implements TransactionType {
    B2B("B2B"),
    B2C("B2C")
    ;

    private String method;

    FuiouTransactionType(String method) {
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