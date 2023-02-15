package com.cn.xiguaapp.r2dbc.orm.rdb.support.mssql;

import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.RDBColumnMetadata;
import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.RDBTableMetadata;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.builder.fragments.PrepareSqlFragments;

/**
 * @author xiguaapp
 */
public abstract class SqlServerCommentUtils {

    private static final String dropTableCommentSql = "IF exists(" +
            "SELECT 1 FROM fn_listextendedproperty" +
            "('MS_Description', 'SCHEMA', ?, 'TABLE',?, NULL, NULL)" +
            " WHERE name = 'MS_Description') " +
            "EXEC sp_dropextendedproperty 'MS_Description', 'SCHEMA', ?, 'TABLE',?";


    private static final String tableCommentSql = "EXEC sp_addextendedproperty 'MS_Description', ?, 'SCHEMA', ?, 'TABLE', ?";

    private static final String dropAndCreateTableComment = dropTableCommentSql.concat(";").concat(tableCommentSql);

    private static final String dropColumnCommentSql = "IF exists(" +
            "SELECT 1 FROM fn_listextendedproperty('MS_Description', 'SCHEMA', ?, 'TABLE', ?, 'column', ?) " +
            "WHERE name = 'MS_Description') " +
            "EXEC sp_dropextendedproperty 'MS_Description', 'SCHEMA', ?, 'TABLE', ?, 'column', ?";


    private static final String columnCommentSql = "EXEC sp_addextendedproperty 'MS_Description', ?, 'SCHEMA',? , 'TABLE',? , 'column',?";
    private static final String dropAndCreateColumnComment = dropColumnCommentSql.concat(";").concat(columnCommentSql);

    public static PrepareSqlFragments createDropAndCreateTableCommentSql(RDBTableMetadata table) {
        String schemaName = table.getSchema().getName();
        String tableName = table.getName();

        return PrepareSqlFragments.of(dropAndCreateTableComment,
                schemaName, tableName, schemaName, tableName,
                table.getComment(), schemaName, tableName
        );
    }

    public static PrepareSqlFragments createDropColumnComment(RDBTableMetadata table, RDBColumnMetadata column) {
        String schemaName = table.getSchema().getName();
        String tableName = table.getName();
        String columnName = column.getName();

        return PrepareSqlFragments.of(dropColumnCommentSql,
                schemaName, tableName, columnName, schemaName, tableName, columnName
        );
    }
    public static PrepareSqlFragments createDropAdnCreateColumnComment(RDBTableMetadata table, RDBColumnMetadata column) {
        String schemaName = table.getSchema().getName();
        String tableName = table.getName();
        String columnName = column.getName();

        return PrepareSqlFragments.of(dropAndCreateColumnComment,
                schemaName, tableName, columnName, schemaName, tableName, columnName, column.getComment(), schemaName, tableName, columnName
        );
    }

}
