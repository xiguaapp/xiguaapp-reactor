package com.cn.xiguaapp.r2dbc.orm.rdb.oprator.builder.fragments.query;


import com.cn.xiguaapp.r2dbc.orm.core.FeatureId;
import com.cn.xiguaapp.r2dbc.orm.rdb.executor.SqlRequest;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.builder.SqlBuilder;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.dml.query.QueryOperatorParameter;

/**
 * @author xiguaapp
 */
public interface QuerySqlBuilder extends SqlBuilder<QueryOperatorParameter> {

    String ID_VALUE = "querySqlBuilder";

    FeatureId<QuerySqlBuilder> ID = FeatureId.of(ID_VALUE);

    @Override
    default String getId() {
        return ID_VALUE;
    }

    @Override
    default String getName() {
        return "查询SQL构造器";
    }

    SqlRequest build(QueryOperatorParameter parameter);


}
