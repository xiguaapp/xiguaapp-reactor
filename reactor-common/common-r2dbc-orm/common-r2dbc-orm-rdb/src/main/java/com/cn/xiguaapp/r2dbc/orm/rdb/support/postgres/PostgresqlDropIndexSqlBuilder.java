package com.cn.xiguaapp.r2dbc.orm.rdb.support.postgres;


import com.cn.xiguaapp.r2dbc.orm.rdb.executor.SqlRequest;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.builder.fragments.PrepareSqlFragments;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.builder.fragments.ddl.CreateIndexParameter;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.builder.fragments.ddl.DropIndexSqlBuilder;

/**
 * @author xiguaapp
 */
public class PostgresqlDropIndexSqlBuilder implements DropIndexSqlBuilder {

    public static final PostgresqlDropIndexSqlBuilder INSTANCE=new PostgresqlDropIndexSqlBuilder();

    @Override
    public SqlRequest build(CreateIndexParameter parameter) {
       return PrepareSqlFragments.of("drop index")
                .addSql(parameter.getTable().getSchema().getName()+"."+parameter.getIndex().getName())
                .toRequest();
    }
}
