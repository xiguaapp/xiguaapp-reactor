package com.cn.xiguaapp.r2dbc.orm.rdb.oprator.builder.fragments.ddl;


import com.cn.xiguaapp.r2dbc.orm.core.FeatureId;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.builder.SqlBuilder;

public interface DropIndexSqlBuilder extends SqlBuilder<CreateIndexParameter> {

    String ID_VALUE = "dropIndexSqlBuilder";
    FeatureId<DropIndexSqlBuilder> ID = FeatureId.of(ID_VALUE);

    @Override
    default String getId() {
        return ID_VALUE;
    }

    @Override
    default String getName() {
        return "删除SQL构造器";
    }
}
