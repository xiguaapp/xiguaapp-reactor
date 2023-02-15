package com.cn.xiguaapp.r2dbc.common.core.annotation;

import java.lang.annotation.*;

/**
 * @author xiguaapp
 * @desc 响应式支持注解类
 * @see com.cn.xiguaapp.r2dbc.orm.rdb.mapping.ReactiveRepository
 * @since 1.0 17:11
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Reactive {
    /**
     * 是否支持响应式 默认为true
     * @return boolean
     */
    boolean enable() default true;
}
