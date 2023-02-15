package com.cn.xiguaapp.r2dbc.orm.rdb.test.supports.oracle;


import com.cn.xiguaapp.r2dbc.orm.rdb.executor.reactive.ReactiveSqlExecutor;
import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.RDBSchemaMetadata;
import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.dialect.Dialect;
import com.cn.xiguaapp.r2dbc.orm.rdb.support.oracle.OracleSchemaMetadata;
import com.cn.xiguaapp.r2dbc.orm.rdb.test.TestJdbcReactiveSqlExecutor;
import com.cn.xiguaapp.r2dbc.orm.rdb.test.supports.BasicReactiveTests;

public class OracleReactiveTests extends BasicReactiveTests {
    @Override
    protected RDBSchemaMetadata getSchema() {
        return new OracleSchemaMetadata("SYSTEM");
    }

    @Override
    protected Dialect getDialect() {
        return Dialect.ORACLE;
    }

    @Override
    protected ReactiveSqlExecutor getSqlExecutor() {

        return new TestJdbcReactiveSqlExecutor(new OracleConnectionProvider());
    }
}
