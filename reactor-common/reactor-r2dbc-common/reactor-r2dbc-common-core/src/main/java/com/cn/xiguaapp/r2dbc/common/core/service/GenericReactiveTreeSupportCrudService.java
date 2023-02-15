package com.cn.xiguaapp.r2dbc.common.core.service;

import com.cn.xiguaapp.r2dbc.common.api.crud.entity.TreeSortSupportEntity;
import com.cn.xiguaapp.r2dbc.orm.rdb.mapping.ReactiveRepository;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author xiguaapp
 * @desc 响应式树形结构增删改查
 * @since 1.0 16:57
 */
public abstract class GenericReactiveTreeSupportCrudService <E extends TreeSortSupportEntity<K>, K> implements ReactiveTreeSortEntityService<E, K> {
    @Autowired
    private ReactiveRepository<E, K> repository;

    @Override
    public ReactiveRepository<E, K> getRepository() {
        return repository;
    }
}
