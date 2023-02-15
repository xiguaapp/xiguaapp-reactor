package com.cn.xiguaapp.r2dbc.orm.rdb.test.supports.postgres;

import com.cn.xiguaapp.r2dbc.orm.rdb.test.R2dbcConnectionProvider;
import io.r2dbc.postgresql.PostgresqlConnectionFactory;
import io.r2dbc.spi.Connection;
import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactoryOptions;
import lombok.SneakyThrows;
import reactor.core.publisher.Mono;

import java.net.URL;
import java.util.function.Supplier;

import static io.r2dbc.spi.ConnectionFactoryOptions.*;

public class Postgresql12R2dbcConnectionProvider implements R2dbcConnectionProvider {


    Supplier<Mono<Connection>> connectionSupplier;

    @SneakyThrows
    public Postgresql12R2dbcConnectionProvider() {

        String username = System.getProperty("postgres.username", "postgres");
        String password = System.getProperty("postgres.password", "admin");
        String url = System.getProperty("postgres.url", "127.0.0.1:15433");
        String db = System.getProperty("postgres.db", "demo");

        URL hostUrl = new URL("file://" + url);

        PostgresqlConnectionFactory connectionFactory = (PostgresqlConnectionFactory) ConnectionFactories.get(ConnectionFactoryOptions.builder()
                .option(DRIVER, "postgresql")
                .option(HOST, hostUrl.getHost())  // file, mem
                .option(PORT, hostUrl.getPort())  // file, mem
                .option(USER, username)
                .option(PASSWORD, password)
                .option(DATABASE, db)
                .build());
        connectionSupplier = () -> connectionFactory.create().map(Connection.class::cast);
    }

    @Override
    public Mono<Connection> getConnection() {
        return connectionSupplier.get();
    }

    @Override
    public void releaseConnection(Connection connection) {
        connection.close();
    }
}
