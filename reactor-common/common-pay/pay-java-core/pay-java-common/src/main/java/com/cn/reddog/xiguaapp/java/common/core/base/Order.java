package com.cn.xiguaapp.xiguaapp.java.common.core.base;

/**
 * @author xiguaapp
 * @desc 支付订单信息
 * @since 1.0 17:09
 */
public interface Order extends Attrs {
    /**
     * 添加订单信息
     *
     * @param key   key
     * @param value 值
     */
    void addAttr(String key, Object value);
}
