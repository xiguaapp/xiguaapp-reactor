package com.cn.xiguaapp.r2dbc.common.core.configuration;

import com.cn.xiguaapp.r2dbc.orm.rdb.mapping.ReactiveRepository;
import com.cn.xiguaapp.r2dbc.orm.rdb.mapping.defaults.DefaultReactiveRepository;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.DatabaseOperator;
import lombok.Data;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author xiguaapp
 * @desc
 * @since 1.0 11:51
 */
@Data
public class ReactiveRepositoryFactoryBean<E,T> implements FactoryBean<ReactiveRepository<E,T>> {
    @Autowired
    private DatabaseOperator operator;

    @Autowired
    private EntityTableMetadataResolver resolver;

    private Class<E> entityType;

    @Autowired
    private EntityResultWrapperFactory wrapperFactory;

    @Override
    public ReactiveRepository<E, T> getObject() {

        return new DefaultReactiveRepository<>(operator,
                resolver.resolve(entityType),
                entityType,
                wrapperFactory.getWrapper(entityType));
    }

    @Override
    public Class<?> getObjectType() {
        return ReactiveRepository.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

}
