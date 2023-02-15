package com.cn.xiguaapp.r2dbc.orm.rdb.oprator.builder.fragments.ddl;


import com.cn.xiguaapp.r2dbc.orm.core.FeatureId;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.builder.SqlBuilder;

public interface AlterTableSqlBuilder extends SqlBuilder<AlterRequest> {

    String ID_VALUE = "alterTableSqlBuilder";

    FeatureId<AlterTableSqlBuilder> ID =FeatureId.of(ID_VALUE);

    @Override
    default String getId() {
        return ID_VALUE;
    }

    @Override
    default String getName() {
        return "表结构更新SQL构造器";
    }
}
