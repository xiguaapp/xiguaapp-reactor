package com.cn.xiguaapp.r2dbc.orm.rdb.oprator.builder.fragments.delete;


import com.cn.xiguaapp.r2dbc.orm.core.FeatureId;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.builder.SqlBuilder;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.dml.delete.DeleteOperatorParameter;

/**
 * @author xiguaapp
 */
public interface DeleteSqlBuilder  extends SqlBuilder<DeleteOperatorParameter> {

    String ID_VALUE = "deleteSqlBuilder";
    FeatureId<DeleteSqlBuilder> ID =FeatureId.of(ID_VALUE);

    @Override
    default String getId() {
        return ID_VALUE;
    }

    @Override
    default String getName() {
        return "Delete SQL 构造器";
    }

}
