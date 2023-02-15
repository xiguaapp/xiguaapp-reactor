package com.cn.xiguaapp.r2dbc.orm.rdb.mapping;


import com.cn.xiguaapp.r2dbc.orm.feature.Feature;

public interface EntityManager extends Feature {

    @Override
    default String getName() {
        return MappingFeatureType.entityManager.getName();
    }

    @Override
    default String getId() {
        return MappingFeatureType.entityManager.getId();
    }

    @Override
    default MappingFeatureType getType() {
        return MappingFeatureType.entityManager;
    }

    <E> E newInstance(Class<E> type);

    EntityColumnMapping getMapping(Class entity);
}
