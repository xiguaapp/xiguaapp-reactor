package com.cn.xiguaapp.r2dbc.orm.rdb.oprator.dml.insert;

import com.cn.xiguaapp.r2dbc.orm.rdb.executor.SqlRequest;
import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.RDBTableMetadata;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.builder.fragments.insert.InsertSqlBuilder;
import lombok.AllArgsConstructor;

@AllArgsConstructor(staticName = "of")
public class ExecutableInsertOperator extends BuildParameterInsertOperator {

    private RDBTableMetadata table;

    @Override
    public SqlRequest getSql() {
        return table
                .findFeatureNow(InsertSqlBuilder.ID)
                .build(getParameter());
    }

    @Override
    public InsertResultOperator execute() {

        return DefaultInsertResultOperator.of(table, this::getSql);

    }
}
