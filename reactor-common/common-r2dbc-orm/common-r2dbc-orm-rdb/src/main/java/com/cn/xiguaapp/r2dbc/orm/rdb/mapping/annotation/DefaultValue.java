package com.cn.xiguaapp.r2dbc.orm.rdb.mapping.annotation;



import com.cn.xiguaapp.r2dbc.orm.core.DefaultValueGenerator;
import com.cn.xiguaapp.r2dbc.orm.core.RuntimeDefaultValue;

import java.lang.annotation.*;

/**
 * @see DefaultValueGenerator
 */
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface DefaultValue {

    /**
     * @return 生成器ID
     * @see DefaultValueGenerator#getId()
     */
    String generator() default "";

    /**
     * @return 固定默认值
     * @see RuntimeDefaultValue#get()
     */
    String value() default "";
}
