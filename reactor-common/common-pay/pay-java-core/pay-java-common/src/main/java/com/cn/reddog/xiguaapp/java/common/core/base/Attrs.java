package com.cn.xiguaapp.xiguaapp.java.common.core.base;

import java.io.Serializable;
import java.util.Map;

/**
 * @author xiguaapp
 * @desc 基本属性信息
 * @since 1.0 14:17
 */
public interface Attrs extends Serializable {
    /**
     * 获取属性 这里可用做覆盖已设置的信息属性，订单信息在签名前进行覆盖。
     *
     * @return 属性
     */
    Map<String, Object> getAttrs();

    /**
     * 获取属性 这里可用做覆盖已设置的订单信息属性，订单信息在签名前进行覆盖。
     *
     * @param key 属性名
     * @return 属性
     */
    Object getAttr(String key);
}
