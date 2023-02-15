package com.cn.xiguaapp.r2dbc.common.core.service;

import com.cn.xiguaapp.r2dbc.orm.rdb.mapping.SyncRepository;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author xiguaapp
 * @desc
 * @since 1.0 16:53
 */
public abstract class GenericCrudService<E,K> implements CrudService<E,K> {
    @Autowired
    private SyncRepository<E, K> repository;

    @Override
    public SyncRepository<E, K> getRepository() {
        return repository;
    }
}
