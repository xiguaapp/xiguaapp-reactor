package com.cn.xiguaapp.r2dbc.orm.rdb.oprator.builder.fragments.ddl;


import com.cn.xiguaapp.r2dbc.orm.core.FeatureId;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.builder.SqlBuilder;

public interface CreateIndexSqlBuilder extends SqlBuilder<CreateIndexParameter> {

    String ID_VALUE = "createIndexSqlBuilder";

    FeatureId<CreateIndexSqlBuilder> ID = FeatureId.of(ID_VALUE);

    @Override
    default String getId() {
        return ID_VALUE;
    }

    @Override
    default String getName() {
        return "索引SQL构造器";
    }
}
