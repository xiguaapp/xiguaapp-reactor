package com.cn.xiguaapp.r2dbc.orm.rdb.oprator.dml.delete;

import com.cn.xiguaapp.r2dbc.orm.rdb.executor.SqlRequest;
import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.RDBTableMetadata;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.builder.fragments.delete.DeleteSqlBuilder;
import lombok.AllArgsConstructor;

/**
 * @author xiguaapp
 * @desc
 */
@AllArgsConstructor(staticName = "of")
public class ExecutableDeleteOperator extends BuildParameterDeleteOperator {

    private RDBTableMetadata table;

    @Override
    public SqlRequest getSql() {
        return table.findFeatureNow(DeleteSqlBuilder.ID).build(getParameter());
    }

    @Override
    public DeleteResultOperator execute() {
        return DefaultDeleteResultOperator.of(table, this::getSql);
    }
}
