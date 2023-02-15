package com.cn.xiguaapp.datasource.api.annotation;

import java.lang.annotation.*;

/**
 * @author xiguaapp
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface UseDefaultDataSource {
}
