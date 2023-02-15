package com.cn.xiguaapp.common.starter.annotation;

import java.lang.annotation.*;

/**
 * @author xiguaapp
 * @Date 2020/10/12
 * @desc
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Open {
    /**
     * 接口名，如：member.user.get
     */
    String value();

    /**
     * 版本号，默认版本号是""<br>
     *     改默认版本号：<code>ServiceConfig.getInstance().setDefaultVersion("1.0");</code>
     */
    String version() default "";

    /**
     * 忽略验证，业务参数除外
     */
    boolean ignoreValidate() default false;

    /**
     * 告诉网关是否对结果进行合并，默认合并。设置为false，客户端将直接收到微服务端的结果。
     */
    boolean mergeResult() default true;

    /**
     * 指定接口是否需要授权才能访问，可在admin中进行修改
     */
    boolean permission() default false;

    /**
     * 是否需要appAuthToken，设置为true，网关端会校验token是否存在
     */
    boolean needToken() default false;

    /**
     * 定义业务错误码，用于文档显示
     */
    BizCode[] bizCode() default {};
}
