package com.cn.xiguaapp.r2dbc.orm.rdb.mapping;


import com.cn.xiguaapp.r2dbc.orm.feature.Feature;
import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.RDBColumnMetadata;

import java.util.Map;
import java.util.Optional;

public interface EntityColumnMapping extends Feature {

    @Override
    default MappingFeatureType getType() {
        return MappingFeatureType.columnPropertyMapping;
    }

    Class<?> getEntityType();

    Optional<RDBColumnMetadata> getColumnByProperty(String property);

    Optional<String> getPropertyByColumnName(String columnName);

    Optional<RDBColumnMetadata> getColumnByName(String columnName);

    Map<String,String> getColumnPropertyMapping();
}
