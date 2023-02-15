package com.cn.xiguaapp.pay.spring.boot.core;

/**
 * @author xiguaapp
 * @desc 支付配置适配，主要用于外部调用者链式的方式创建对象
 * @since 1.0 23:34
 */
public interface PayConfigurerAdapter<T> {

    /**
     * 外部调用者使用，链式的做法
     *
     * @return 返回对应外部调用者
     */
    T and();

    /**
     * 获取构建器
     * @return 构建器
     */
    T getBuilder();
}

