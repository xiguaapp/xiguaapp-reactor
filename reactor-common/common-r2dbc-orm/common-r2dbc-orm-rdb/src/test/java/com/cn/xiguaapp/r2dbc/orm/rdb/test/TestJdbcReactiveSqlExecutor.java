package com.cn.xiguaapp.r2dbc.orm.rdb.test;

import com.cn.xiguaapp.r2dbc.orm.rdb.executor.SqlRequest;
import com.cn.xiguaapp.r2dbc.orm.rdb.executor.jdbc.JdbcReactiveSqlExecutor;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

import java.sql.Connection;

@AllArgsConstructor
public class TestJdbcReactiveSqlExecutor extends JdbcReactiveSqlExecutor {

    private ConnectionProvider provider;

    @Override
    public Mono<Connection> getConnection(SqlRequest sqlRequest) {
        return Mono.fromSupplier(provider::getConnection);
    }

    @Override
    public void releaseConnection(Connection connection, SqlRequest sqlRequest) {
        provider.releaseConnect(connection);
    }
}
