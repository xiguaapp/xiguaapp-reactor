package com.cn.xiguaapp.r2dbc.orm.rdb.oprator.dml.update;

import com.cn.xiguaapp.r2dbc.orm.rdb.executor.SqlRequest;
import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.RDBTableMetadata;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.builder.fragments.update.UpdateSqlBuilder;
import lombok.AllArgsConstructor;

/**
 * @author xiguaapp
 */
@AllArgsConstructor(staticName = "of")
public class ExecutableUpdateOperator extends BuildParameterUpdateOperator {

    private RDBTableMetadata table;

    @Override
    public SqlRequest getSql() {
        return table.<UpdateSqlBuilder>findFeatureNow(UpdateSqlBuilder.ID_VALUE)
                .build(getParameter());
    }

    @Override
    public UpdateResultOperator execute() {
        return DefaultUpdateResultOperator.of(table,this::getSql);
    }
}
