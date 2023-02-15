package com.cn.xiguaapp.r2dbc.orm.rdb.test.supports.mssql;


import com.cn.xiguaapp.r2dbc.orm.rdb.executor.reactive.ReactiveSqlExecutor;
import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.RDBSchemaMetadata;
import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.dialect.Dialect;
import com.cn.xiguaapp.r2dbc.orm.rdb.support.mssql.SqlServerSchemaMetadata;
import com.cn.xiguaapp.r2dbc.orm.rdb.test.TestReactiveSqlExecutor;
import com.cn.xiguaapp.r2dbc.orm.rdb.test.supports.BasicReactiveTests;

public class MSSQLReactiveTests extends BasicReactiveTests {
    @Override
    protected RDBSchemaMetadata getSchema() {
        return new SqlServerSchemaMetadata("dbo");
    }

    @Override
    protected Dialect getDialect() {
        return Dialect.MSSQL;
    }

    @Override
    protected ReactiveSqlExecutor getSqlExecutor() {

        return new TestReactiveSqlExecutor("@arg",new MSSQLR2dbcConnectionProvider());
    }
}
