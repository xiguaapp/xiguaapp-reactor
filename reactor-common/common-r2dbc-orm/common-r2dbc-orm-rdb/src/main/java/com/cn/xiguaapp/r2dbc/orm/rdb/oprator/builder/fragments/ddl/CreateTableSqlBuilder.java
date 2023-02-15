package com.cn.xiguaapp.r2dbc.orm.rdb.oprator.builder.fragments.ddl;

import com.cn.xiguaapp.r2dbc.orm.core.FeatureId;
import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.RDBTableMetadata;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.builder.SqlBuilder;

/**
 * @author xiguaapp
 */
public interface CreateTableSqlBuilder extends SqlBuilder<RDBTableMetadata> {

    String ID_VALUE = "createTableSqlBuilder";
    FeatureId<CreateTableSqlBuilder> ID =FeatureId.of(ID_VALUE);

    @Override
    default String getId() {
        return ID_VALUE;
    }

    @Override
    default String getName() {
        return "Create Table SQL 构造器";
    }
}
