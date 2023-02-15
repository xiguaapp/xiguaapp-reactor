package com.cn.xiguaapp.r2dbc.orm.rdb.mapping.annotation;

import java.lang.annotation.*;

/**
 * @author xiguaapp
 * @see JsonCodec
 * @see EnumCodec
 * @see DateTimeCodec
 */
@Target({ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Codec {

}
