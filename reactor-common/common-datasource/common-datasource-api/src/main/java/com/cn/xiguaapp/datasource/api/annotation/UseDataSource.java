package com.cn.xiguaapp.datasource.api.annotation;

import com.cn.xiguaapp.datasource.api.core.DataSourceHolder;
import com.cn.xiguaapp.datasource.api.core.DynamicDataSource;
import com.cn.xiguaapp.datasource.api.exception.DataSourceNotFoundException;

import java.lang.annotation.*;

/**
 * @author xiguaapp
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface UseDataSource {
    /**
     * @return 数据源ID ,支持表达式如 : ${#param.id}
     * @see DynamicDataSource#getId()
     */
    String value() default "";

    /**
     * 指定数据库
     *
     * @return 数据库名
     */
    String database() default "";

    /**
     * @return 数据源不存在时, 是否使用默认数据源.
     * 如果为{@code false},当数据源不存在的时候,
     * 将抛出 {@link DataSourceNotFoundException}
     * @see DataSourceHolder#currentExisting()
     */
    boolean fallbackDefault() default false;
}
