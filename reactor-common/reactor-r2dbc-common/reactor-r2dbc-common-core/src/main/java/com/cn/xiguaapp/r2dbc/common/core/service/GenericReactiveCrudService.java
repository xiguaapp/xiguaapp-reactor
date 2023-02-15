package com.cn.xiguaapp.r2dbc.common.core.service;

import com.cn.xiguaapp.r2dbc.orm.rdb.mapping.ReactiveRepository;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author xiguaapp
 * @desc 不带缓存的增删改查方法 详情查看 {@link ReactiveCrudService}
 * @since 1.0 16:54
 */
public abstract class GenericReactiveCrudService<E, K> implements ReactiveCrudService<E, K> {
    @Autowired
    private ReactiveRepository<E, K> repository;

    @Override
    public ReactiveRepository<E, K> getRepository() {
        return repository;
    }
}
