package com.cn.xiguaapp.r2dbc.orm.rdb.support.mssql;

import com.cn.xiguaapp.r2dbc.orm.core.DefaultValue;
import com.cn.xiguaapp.r2dbc.orm.rdb.executor.DefaultBatchSqlRequest;
import com.cn.xiguaapp.r2dbc.orm.rdb.executor.SqlRequest;
import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.RDBColumnMetadata;
import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.RDBIndexMetadata;
import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.RDBTableMetadata;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.builder.fragments.NativeSql;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.builder.fragments.PrepareSqlFragments;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.builder.fragments.ddl.CreateIndexParameter;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.builder.fragments.ddl.CreateIndexSqlBuilder;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.builder.fragments.ddl.CreateTableSqlBuilder;
import lombok.Getter;
import lombok.Setter;

@SuppressWarnings("all")
@Getter
@Setter
public class SqlServerCreateTableSqlBuilder implements CreateTableSqlBuilder {

    private boolean createComment = true;

    @Override
    public SqlRequest build(RDBTableMetadata table) {
        DefaultBatchSqlRequest sql = new DefaultBatchSqlRequest();

        PrepareSqlFragments createTable = PrepareSqlFragments.of();

        createTable.addSql("create table", table.getFullName(), "(");

        int index = 0;
        for (RDBColumnMetadata column : table.getColumns()) {
            if (index++ != 0) {
                createTable.addSql(",");
            }
            createTable.addSql(column.getQuoteName());
            if (column.getColumnDefinition() != null) {
                createTable.addSql(column.getColumnDefinition());
            } else {
                createTable.addSql(column.getDialect().buildColumnDataType(column));
                if (column.isNotNull() || column.isPrimaryKey()) {
                    createTable.addSql("not null");
                }
                if (column.isPrimaryKey()) {
                    createTable.addSql("primary key");
                }
                DefaultValue defaultValue = column.getDefaultValue();
                if (defaultValue instanceof NativeSql) {
                    createTable.addSql("default", ((NativeSql) defaultValue).getSql());
                }

            }
            if (createComment && column.getComment() != null) {
                sql.addBatch(SqlServerCommentUtils.createDropAdnCreateColumnComment(table, column).toRequest());
            }

        }
        createTable.addSql(")");
        if (createComment && table.getComment() != null) {
            sql.addBatch(SqlServerCommentUtils.createDropAndCreateTableCommentSql(table).toRequest());
        }
        table.findFeature(CreateIndexSqlBuilder.ID)
                .ifPresent(builder -> {
                    for (RDBIndexMetadata tableIndex : table.getIndexes()) {
                        sql.addBatch(builder.build(CreateIndexParameter.of(table, tableIndex)));
                    }
                });

        sql.setSql(createTable.toRequest().getSql());

        return sql;
    }

}
