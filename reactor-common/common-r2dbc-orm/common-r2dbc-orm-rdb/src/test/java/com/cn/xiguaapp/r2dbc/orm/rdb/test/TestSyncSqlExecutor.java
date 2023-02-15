package com.cn.xiguaapp.r2dbc.orm.rdb.test;

import com.cn.xiguaapp.r2dbc.orm.rdb.executor.SqlRequest;
import com.cn.xiguaapp.r2dbc.orm.rdb.executor.jdbc.JdbcSyncSqlExecutor;
import lombok.AllArgsConstructor;

import java.sql.Connection;

@AllArgsConstructor
public class TestSyncSqlExecutor extends JdbcSyncSqlExecutor {

    private ConnectionProvider provider;


    @Override
    public Connection getConnection(SqlRequest sqlRequest) {
        return provider.getConnection();
    }

    @Override
    public void releaseConnection(Connection connection, SqlRequest sqlRequest) {
        provider.releaseConnect(connection);
    }
}
