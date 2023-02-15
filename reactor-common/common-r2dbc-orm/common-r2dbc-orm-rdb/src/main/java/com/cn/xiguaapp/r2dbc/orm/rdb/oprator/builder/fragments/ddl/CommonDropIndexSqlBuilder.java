package com.cn.xiguaapp.r2dbc.orm.rdb.oprator.builder.fragments.ddl;


import com.cn.xiguaapp.r2dbc.orm.rdb.executor.SqlRequest;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.builder.fragments.PrepareSqlFragments;

/**
 * @author xiguaapp
 */
public class CommonDropIndexSqlBuilder implements DropIndexSqlBuilder {

    public static final CommonDropIndexSqlBuilder INSTANCE=new CommonDropIndexSqlBuilder();

    @Override
    public SqlRequest build(CreateIndexParameter parameter) {
       return PrepareSqlFragments.of("drop index")
                .addSql(parameter.getIndex().getName(), "on", parameter.getTable().getFullName())
                .toRequest();
    }
}
