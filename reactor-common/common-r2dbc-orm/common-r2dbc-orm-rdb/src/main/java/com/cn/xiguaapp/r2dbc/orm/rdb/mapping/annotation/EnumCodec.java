package com.cn.xiguaapp.r2dbc.orm.rdb.mapping.annotation;

import com.cn.xiguaapp.r2dbc.orm.rdb.codec.EnumValueCodec;

import java.lang.annotation.*;

/**
 * @author xiguaapp
 * @see EnumValueCodec
 */
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@Codec
public @interface EnumCodec {

    /**
     * @return 是否使用将枚举的序号进行位掩码以实现多选
     * @see java.sql.JDBCType#NUMERIC
     * @see Long
     */
    boolean toMask() default false;

}
