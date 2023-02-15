package com.cn.xiguaapp.common.oauth.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author xiguaapp
 * @desc 登录校验：
 *      标注在一个方法上，当前会话必须已经登录才能进入该方法
 *      可标注在类上，其效果等同于标注在此类的所有方法上
 * @since 1.0 00:57
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.TYPE })
public @interface SaCheckLogin {

}
