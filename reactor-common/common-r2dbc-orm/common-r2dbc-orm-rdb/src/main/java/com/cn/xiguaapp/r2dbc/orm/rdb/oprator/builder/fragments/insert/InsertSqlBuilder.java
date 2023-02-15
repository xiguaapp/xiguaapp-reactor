package com.cn.xiguaapp.r2dbc.orm.rdb.oprator.builder.fragments.insert;

import com.cn.xiguaapp.r2dbc.orm.core.FeatureId;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.builder.SqlBuilder;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.dml.insert.InsertOperatorParameter;

/**
 * @author xiguaapp
 */
public interface InsertSqlBuilder extends SqlBuilder<InsertOperatorParameter> {

    String ID_VALUE = "insertSqlBuilder";

    FeatureId<InsertSqlBuilder> ID = FeatureId.of(ID_VALUE);

    @Override
    default String getId() {
        return ID_VALUE;
    }

    @Override
    default String getName() {
        return "Insert SQL 构造器";
    }
}
