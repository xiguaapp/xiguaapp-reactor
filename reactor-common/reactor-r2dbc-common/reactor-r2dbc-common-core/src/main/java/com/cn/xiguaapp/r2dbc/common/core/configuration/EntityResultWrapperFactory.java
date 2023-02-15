package com.cn.xiguaapp.r2dbc.common.core.configuration;

import com.cn.xiguaapp.r2dbc.orm.rdb.executor.wrapper.ResultWrapper;

/**
 * @author xiguaapp
 * @desc
 * @since 1.0 18:00
 */
public interface EntityResultWrapperFactory {
    <T> ResultWrapper<T, ?> getWrapper(Class<T> tClass);
}
