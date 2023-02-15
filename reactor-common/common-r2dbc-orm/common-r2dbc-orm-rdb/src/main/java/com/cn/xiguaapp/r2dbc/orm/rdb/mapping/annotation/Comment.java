package com.cn.xiguaapp.r2dbc.orm.rdb.mapping.annotation;

import java.lang.annotation.*;

/**
 * @author xiguaapp
 * @desc 数据库字段备注(备注)
 * @since 1.0
 */
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Comment {
    String value();
}
