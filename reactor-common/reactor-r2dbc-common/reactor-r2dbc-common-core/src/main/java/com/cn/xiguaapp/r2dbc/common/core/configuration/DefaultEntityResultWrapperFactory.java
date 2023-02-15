package com.cn.xiguaapp.r2dbc.common.core.configuration;

import com.cn.xiguaapp.r2dbc.orm.rdb.executor.wrapper.ResultWrapper;
import com.cn.xiguaapp.r2dbc.orm.rdb.mapping.EntityManager;
import com.cn.xiguaapp.r2dbc.orm.rdb.mapping.wrapper.EntityResultWrapper;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

/**
 * @author xiguaapp
 * @desc
 * @since 1.0 18:00
 */
@AllArgsConstructor
public class DefaultEntityResultWrapperFactory implements EntityResultWrapperFactory {
    private final EntityManager entityManager;

    @Override
    @SneakyThrows
    public <T> ResultWrapper<T, ?> getWrapper(Class<T> tClass) {
        return new EntityResultWrapper<>(() -> entityManager.newInstance(tClass),
                entityManager.getMapping(tClass));

    }
}
