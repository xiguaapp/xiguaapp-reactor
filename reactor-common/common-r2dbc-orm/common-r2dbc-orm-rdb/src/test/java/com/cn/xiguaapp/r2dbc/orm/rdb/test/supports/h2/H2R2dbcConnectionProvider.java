package com.cn.xiguaapp.r2dbc.orm.rdb.test.supports.h2;

import com.cn.xiguaapp.r2dbc.orm.rdb.test.R2dbcConnectionProvider;
import io.r2dbc.h2.H2ConnectionFactory;
import io.r2dbc.spi.Connection;
import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactoryOptions;
import reactor.core.publisher.Mono;

import static io.r2dbc.spi.ConnectionFactoryOptions.*;

public class H2R2dbcConnectionProvider implements R2dbcConnectionProvider {

    private Connection connection;

    public H2R2dbcConnectionProvider() {
        H2ConnectionFactory connectionFactory = (H2ConnectionFactory) ConnectionFactories.get(ConnectionFactoryOptions.builder()
                .option(DRIVER, "h2")
                .option(PROTOCOL, "mem")  // file, mem
                .option(DATABASE, "test_"+Math.random())
                .build());
        connection=connectionFactory.create().block();
    }

    @Override
    public Mono<Connection> getConnection() {
        return Mono.just(connection);
    }

    @Override
    public void releaseConnection(Connection connection) {

    }
}
