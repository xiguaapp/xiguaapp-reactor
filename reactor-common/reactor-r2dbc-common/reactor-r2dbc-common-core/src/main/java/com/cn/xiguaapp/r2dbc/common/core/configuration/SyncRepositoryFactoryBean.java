package com.cn.xiguaapp.r2dbc.common.core.configuration;

import com.cn.xiguaapp.r2dbc.orm.rdb.mapping.SyncRepository;
import com.cn.xiguaapp.r2dbc.orm.rdb.mapping.defaults.DefaultSyncRepository;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.DatabaseOperator;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author xiguaapp
 * @desc
 * @since 1.0 15:18
 */
@Getter
@Setter
public class SyncRepositoryFactoryBean<E,T> implements FactoryBean<SyncRepository<E,T>> {
    @Autowired
    private DatabaseOperator operator;

    @Autowired
    private EntityTableMetadataResolver resolver;

    @Autowired
    private EntityResultWrapperFactory wrapperFactory;

    private Class<E> entityType;

    @Override
    public SyncRepository<E, T> getObject() {

        return new DefaultSyncRepository<>(operator,
                resolver.resolve(entityType),
                entityType,
                wrapperFactory.getWrapper(entityType));
    }

    @Override
    public Class<?> getObjectType() {
        return SyncRepository.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
