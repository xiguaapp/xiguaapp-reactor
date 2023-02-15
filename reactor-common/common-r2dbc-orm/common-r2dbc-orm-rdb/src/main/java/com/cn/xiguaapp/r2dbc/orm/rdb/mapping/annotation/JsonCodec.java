package com.cn.xiguaapp.r2dbc.orm.rdb.mapping.annotation;

import com.cn.xiguaapp.r2dbc.orm.rdb.codec.JsonValueCodec;

import java.lang.annotation.*;

/**
 * @author xiguaapp
 * @see JsonValueCodec
 * @see Codec
 */
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@Codec
public @interface JsonCodec {

}
