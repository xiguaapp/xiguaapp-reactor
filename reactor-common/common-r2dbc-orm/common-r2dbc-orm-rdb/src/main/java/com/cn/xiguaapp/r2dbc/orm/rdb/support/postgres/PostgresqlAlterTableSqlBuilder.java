package com.cn.xiguaapp.r2dbc.orm.rdb.support.postgres;


import com.cn.xiguaapp.r2dbc.orm.core.DefaultValue;
import com.cn.xiguaapp.r2dbc.orm.rdb.executor.DefaultBatchSqlRequest;
import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.RDBColumnMetadata;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.builder.fragments.NativeSql;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.builder.fragments.PrepareSqlFragments;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.builder.fragments.ddl.CommonAlterTableSqlBuilder;

/**
 * @author xiguaapp
 */
@SuppressWarnings("all")
public class PostgresqlAlterTableSqlBuilder extends CommonAlterTableSqlBuilder {


    @Override
    protected void appendAlterColumnSql(DefaultBatchSqlRequest batch, RDBColumnMetadata oldColumn, RDBColumnMetadata newColumn) {
        PrepareSqlFragments fragments = PrepareSqlFragments.of();

        fragments.addSql("alter table", oldColumn.getOwner().getFullName(), "alter column", oldColumn.getQuoteName());

        if (newColumn.getColumnDefinition() != null) {
            fragments.addSql(newColumn.getColumnDefinition());
        } else {
            fragments.addSql("type", newColumn.getDataType(), "using",
                    newColumn.getQuoteName().concat("::").concat(newColumn.getDataType()));

            DefaultValue defaultValue = newColumn.getDefaultValue();

            if (defaultValue instanceof NativeSql) {
                fragments.addSql("default", ((NativeSql) defaultValue).getSql());
            }
        }

        batch.addBatch(fragments.toRequest());
        if (oldColumn.isNotNull() != newColumn.isNotNull()) {
            batch.addBatch(PrepareSqlFragments.of()
                    .addSql("alter table", newColumn.getOwner().getFullName(),
                            "alter column", newColumn.getQuoteName(), newColumn.isNotNull() ? "set not null" : "drop not null").toRequest());

        }
    }


}
