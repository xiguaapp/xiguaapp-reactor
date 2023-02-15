package com.cn.xiguaapp.r2dbc.common.core.entity.factory;

/**
 * @author xiguaapp
 * @desc 属性复制接口,用于自定义属性复制
 * @since 1.0 15:51
 */
public interface PropertyCopier<S, T> {
    T copyProperties(S source, T target);
}
