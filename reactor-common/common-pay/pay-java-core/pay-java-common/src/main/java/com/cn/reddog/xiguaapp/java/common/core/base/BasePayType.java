package com.cn.xiguaapp.xiguaapp.java.common.core.base;

/**
 * @author xiguaapp
 * @desc 基础支付类型
 * @since 1.0 14:20
 */
public interface BasePayType {
    /**
     * 根据支付类型获取交易类型
     * @param transactionType 类型值
     * @return  交易类型
     */
    TransactionType getTransactionType(String transactionType);
}
