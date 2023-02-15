package com.cn.xiguaapp.xiguaapp.java.common.core.base;

import java.util.Map;

/**
 * @author xiguaapp
 * @desc 转账类型
 * @since 1.0 17:15
 */
public interface TransferType extends TransactionType {
    /**
     * 设置属性
     *
     * @param attr 已有属性对象
     * @param order 转账订单
     * @return 属性对象
     */
    Map<String, Object> setAttr(Map<String, Object> attr, TransferOrder order);
}
