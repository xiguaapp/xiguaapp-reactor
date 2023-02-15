package com.cn.xiguaapp.common.starter.annotation;

import java.lang.annotation.*;

/**
 * @author xiguaapp
 * @Date 2020/10/12
 * @desc 错误码
 */
@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface BizCode {
    /**
     * 错误码
     *
     * @return
     */
    String code();

    /**
     * 错误描述
     * @return
     */
    String msg();

    /**
     * 解决方案
     * @return
     */
    String solution() default "";
}
