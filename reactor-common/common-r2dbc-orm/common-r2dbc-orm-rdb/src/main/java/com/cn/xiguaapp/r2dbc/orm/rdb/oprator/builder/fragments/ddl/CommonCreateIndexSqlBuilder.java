package com.cn.xiguaapp.r2dbc.orm.rdb.oprator.builder.fragments.ddl;

import com.cn.xiguaapp.r2dbc.orm.rdb.executor.SqlRequest;
import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.RDBIndexMetadata;
import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.RDBTableMetadata;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.builder.fragments.PrepareSqlFragments;

/**
 * @author xiguaapp
 */
public class CommonCreateIndexSqlBuilder implements CreateIndexSqlBuilder {

    public static final CommonCreateIndexSqlBuilder INSTANCE = new CommonCreateIndexSqlBuilder();


    @Override
    public SqlRequest build(CreateIndexParameter parameter) {
        RDBIndexMetadata index = parameter.getIndex();
        RDBTableMetadata table = parameter.getTable();
        PrepareSqlFragments fragments = PrepareSqlFragments.of()
                .addSql("create index", index.getName(), "on", table.getFullName(), "(");
        int i = 0;
        for (RDBIndexMetadata.IndexColumn column : index.getColumns()) {
            if (i++ != 0) {
                fragments.addSql(",");
            }
            fragments.addSql(table.getDialect().quote(column.getColumn()))
                    .addSql(column.getSort().name());
        }
        return fragments.addSql(")").toRequest();
    }
}
