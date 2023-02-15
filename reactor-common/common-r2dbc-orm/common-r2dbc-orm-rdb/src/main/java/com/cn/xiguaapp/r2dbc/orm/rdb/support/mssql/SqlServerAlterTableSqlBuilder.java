package com.cn.xiguaapp.r2dbc.orm.rdb.support.mssql;

import com.cn.xiguaapp.r2dbc.orm.core.DefaultValue;
import com.cn.xiguaapp.r2dbc.orm.rdb.executor.DefaultBatchSqlRequest;
import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.RDBColumnMetadata;
import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.RDBTableMetadata;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.builder.fragments.NativeSql;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.builder.fragments.PrepareSqlFragments;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.builder.fragments.ddl.CommonAlterTableSqlBuilder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@SuppressWarnings("all")
public class SqlServerAlterTableSqlBuilder extends CommonAlterTableSqlBuilder {

    private boolean changeComment = true;

    @Override
    protected void appendDropColumnSql(DefaultBatchSqlRequest batch, RDBColumnMetadata drop) {

        //先删除注释
        batch.addBatch(SqlServerCommentUtils.createDropColumnComment(((RDBTableMetadata) drop.getOwner()), drop).toRequest());
        super.appendDropColumnSql(batch, drop);
    }

    @Override
    protected void appendAddColumnCommentSql(DefaultBatchSqlRequest batch, RDBColumnMetadata column) {
        if (changeComment) {
            batch.addBatch(SqlServerCommentUtils.createDropAdnCreateColumnComment(((RDBTableMetadata) column.getOwner()), column).toRequest());
        }
    }

    @Override
    protected PrepareSqlFragments createAlterColumnFragments(RDBColumnMetadata oldColumn, RDBColumnMetadata newColumn) {
        PrepareSqlFragments fragments = PrepareSqlFragments.of();

        fragments.addSql("alter table", oldColumn.getOwner().getFullName(), "alter column", oldColumn.getQuoteName());

        if (newColumn.getColumnDefinition() != null) {
            fragments.addSql(newColumn.getColumnDefinition());
        } else {
            fragments.addSql(newColumn.getDataType(), newColumn.isNotNull() ? "not null" : "null");
            DefaultValue defaultValue = newColumn.getDefaultValue();

            if (defaultValue instanceof NativeSql) {
                fragments.addSql("default", ((NativeSql) defaultValue).getSql());
            }
        }

        return fragments;
    }


}
