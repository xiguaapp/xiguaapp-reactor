package com.cn.xiguaapp.r2dbc.orm.rdb.mapping.annotation;

import java.lang.annotation.*;

/**
 * @author xiguaapp
 */
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@Codec
public @interface DateTimeCodec {
    String format() default "yyyy-MM-dd HH:mm:ss";
}
