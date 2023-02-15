package com.cn.xiguaapp.r2dbc.common.api.crud.entity;

import com.cn.xiguapp.common.core.core.proxy.FastBeanCopier;
import com.cn.xiguapp.common.core.utils.ValidatorUtils;

import java.io.Serializable;

/**
 * @author xiguaapp
 * @desc 实体总接口 所有实体需要实现此接口才能进行后续操作
 * @since 1.0 22:21
 */
public interface Entity extends Serializable {
    default void tryValidate(Class<?>... groups){
        ValidatorUtils.tryValidate(this,groups);
    }
    default <T> T copyTo(Class<T> target, String... ignoreProperties) {
        return FastBeanCopier.copy(this, target, ignoreProperties);
    }

    @SuppressWarnings("all")
    default <T> T copyFrom(Object target, String... ignoreProperties) {
        return (T) FastBeanCopier.copy(target, this, ignoreProperties);
    }
}
