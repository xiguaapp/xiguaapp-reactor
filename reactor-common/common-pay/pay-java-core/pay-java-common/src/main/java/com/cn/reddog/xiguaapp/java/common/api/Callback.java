package com.cn.xiguaapp.xiguaapp.java.common.api;

import java.util.Map;

/**
 * @author xiguaapp
 * @desc 回调，可用于类型转换
 * @since 1.0 18:38
 */
public interface Callback<T> {
    /**
     * 执行者
     * @param map 需要转化的map
     * @return 处理过后的类型对象
     */
    T perform(Map<String, Object> map);
}
