package com.cn.xiguaapp.r2dbc.orm.rdb.test.supports.mssql;


import com.cn.xiguaapp.r2dbc.orm.rdb.executor.SyncSqlExecutor;
import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.RDBSchemaMetadata;
import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.dialect.Dialect;
import com.cn.xiguaapp.r2dbc.orm.rdb.support.mssql.SqlServerPaginator;
import com.cn.xiguaapp.r2dbc.orm.rdb.support.mssql.SqlServerSchemaMetadata;
import com.cn.xiguaapp.r2dbc.orm.rdb.test.TestSyncSqlExecutor;
import com.cn.xiguaapp.r2dbc.orm.rdb.test.supports.BasicCommonTests;

public class MSSQLBasic2Test extends BasicCommonTests {

    @Override
    protected RDBSchemaMetadata getSchema() {
        SqlServerSchemaMetadata schemaMetadata= new SqlServerSchemaMetadata("dbo");
        schemaMetadata.addFeature(new SqlServerPaginator());
        return schemaMetadata;
    }

    @Override
    protected Dialect getDialect() {
        return Dialect.MSSQL;
    }

    @Override
    protected SyncSqlExecutor getSqlExecutor() {
        return new TestSyncSqlExecutor(new MSSQLConnectionProvider());
    }
}
