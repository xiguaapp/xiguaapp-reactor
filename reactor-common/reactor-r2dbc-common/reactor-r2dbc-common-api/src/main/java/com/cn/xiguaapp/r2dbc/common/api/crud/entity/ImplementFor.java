package com.cn.xiguaapp.r2dbc.common.api.crud.entity;

import java.lang.annotation.*;

/**
 * @author xiguaapp
 * @desc
 * @since 1.0 22:24
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface ImplementFor {
    Class value();

    Class idType() default Void.class;
}
