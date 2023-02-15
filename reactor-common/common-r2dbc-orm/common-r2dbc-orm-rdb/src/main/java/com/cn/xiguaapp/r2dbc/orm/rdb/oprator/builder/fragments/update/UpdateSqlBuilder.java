package com.cn.xiguaapp.r2dbc.orm.rdb.oprator.builder.fragments.update;


import com.cn.xiguaapp.r2dbc.orm.core.FeatureId;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.builder.SqlBuilder;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.dml.update.UpdateOperatorParameter;

/**
 * @author xiguaapp
 */
public interface UpdateSqlBuilder extends SqlBuilder<UpdateOperatorParameter> {

    String ID_VALUE = "updateSqlBuilder";

    FeatureId<UpdateSqlBuilder> ID = FeatureId.of(ID_VALUE);

    @Override
    default String getId() {
        return ID_VALUE;
    }

    @Override
    default String getName() {
        return "Update SQL 构造器";
    }
}
