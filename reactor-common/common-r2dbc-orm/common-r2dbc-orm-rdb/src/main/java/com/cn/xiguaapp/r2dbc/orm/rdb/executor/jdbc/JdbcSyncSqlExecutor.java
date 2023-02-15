package com.cn.xiguaapp.r2dbc.orm.rdb.executor.jdbc;

import com.cn.xiguaapp.r2dbc.orm.rdb.executor.SqlRequest;
import com.cn.xiguaapp.r2dbc.orm.rdb.executor.SyncSqlExecutor;
import com.cn.xiguaapp.r2dbc.orm.rdb.executor.wrapper.ResultWrapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;


@Slf4j
public abstract class JdbcSyncSqlExecutor extends JdbcSqlExecutor implements SyncSqlExecutor {

    public JdbcSyncSqlExecutor() {
        super(log);
    }

    public abstract Connection getConnection(SqlRequest sqlRequest);

    public abstract void releaseConnection(Connection connection, SqlRequest sqlRequest);

    @SneakyThrows
    public void releaseStatement(Statement statement) {
        statement.close();
    }

    @SneakyThrows
    public void releaseResultSet(ResultSet resultSet) {
        resultSet.close();
    }

    @Override
    @SneakyThrows
    public int update(SqlRequest request) {
        Connection connection = getConnection(request);
        try {
            return doUpdate(connection, request);
        } finally {
            releaseConnection(connection, request);
        }
    }

    @Override
    @SneakyThrows
    public void execute(SqlRequest request) {
        Connection connection = getConnection(request);
        try {
            doExecute(connection, request);
        } finally {
            releaseConnection(connection, request);
        }
    }

    @Override
    @SneakyThrows
    public <T, R> R select(SqlRequest request, ResultWrapper<T, R> wrapper) {
        Connection connection = getConnection(request);
        try {
            return doSelect(connection, request, wrapper);
        } finally {
            releaseConnection(connection, request);
        }
    }

}
