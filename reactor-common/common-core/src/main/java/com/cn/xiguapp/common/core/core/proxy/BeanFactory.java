package com.cn.xiguapp.common.core.core.proxy;

import com.cn.xiguapp.common.core.exception.NotFoundException;

/**
 * @author xiguaapp
 */
public interface BeanFactory {

    <T> T newInstance(Class<T> beanType) throws NotFoundException;
}
