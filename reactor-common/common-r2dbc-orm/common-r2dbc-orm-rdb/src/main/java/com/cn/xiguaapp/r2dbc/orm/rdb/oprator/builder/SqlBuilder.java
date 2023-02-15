package com.cn.xiguaapp.r2dbc.orm.rdb.oprator.builder;


import com.cn.xiguaapp.r2dbc.orm.feature.Feature;
import com.cn.xiguaapp.r2dbc.orm.rdb.executor.SqlRequest;
import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.RDBFeatureType;

import static com.cn.xiguaapp.r2dbc.orm.rdb.metadata.RDBFeatureType.sqlBuilder;

public interface SqlBuilder<T> extends Feature {
    @Override
    default RDBFeatureType getType() {
        return sqlBuilder;
    }

    SqlRequest build(T parameter);
}
