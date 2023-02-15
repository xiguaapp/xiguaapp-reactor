package com.cn.xiguaapp.r2dbc.orm.rdb.support.mysql;


import com.cn.xiguaapp.r2dbc.orm.rdb.executor.DefaultBatchSqlRequest;
import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.RDBColumnMetadata;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.builder.fragments.PrepareSqlFragments;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.builder.fragments.ddl.CommonAlterTableSqlBuilder;

/**
 * @author xiguaapp
 */
public class MysqlAlterTableSqlBuilder extends CommonAlterTableSqlBuilder {

    @Override
    protected PrepareSqlFragments createAlterColumnFragments(RDBColumnMetadata oldColumn, RDBColumnMetadata newColumn) {

        if (newColumn.getComment() != null) {
            return super.createAlterColumnFragments(oldColumn, newColumn)
                    .addSql("comment", "'".concat(newColumn.getComment()).concat("'"));
        }
        return super.createAlterColumnFragments(oldColumn, newColumn);
    }

    @Override
    protected PrepareSqlFragments createAddColumnFragments(RDBColumnMetadata column) {
        if (column.getComment() != null) {
            return super.createAddColumnFragments(column)
                    .addSql("comment", "'".concat(column.getComment()).concat("'"));
        }
        return super.createAddColumnFragments(column);
    }

    @Override
    protected void appendAddColumnCommentSql(DefaultBatchSqlRequest batch, RDBColumnMetadata column) {

    }
}
