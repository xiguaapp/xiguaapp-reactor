package com.cn.xiguapp.common.core.core.proxy;

/**
 * @author xiguaapp
 * @desc 动态格式转换器
 * @date 2021-01-27 17:41:42
 */
@FunctionalInterface
public interface Converter {
    /**
     * 把响应Object对象转化为响应对象
     * @param source object对象
     * @param targetClass 领域类型
     * @param genericType 领域类型
     * @param <T> 领域泛型
     * @return 领域对象
     */
    <T> T convert(Object source, Class<T> targetClass,Class[] genericType);


}