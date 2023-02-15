package com.cn.xiguaapp.pay.spring.boot.core;

/**
 * @author xiguaapp
 * @desc 用于构建对象的接口
 * @param <e> 正在构建的对象的类型
 * @since 1.0 23:33
 */
public interface PayBuilder<e> {


    /**
     * 构建对象并返回它或null。
     *
     * @return 如果实现允许，则要构建的对象或null。
     */
    e build();
}
