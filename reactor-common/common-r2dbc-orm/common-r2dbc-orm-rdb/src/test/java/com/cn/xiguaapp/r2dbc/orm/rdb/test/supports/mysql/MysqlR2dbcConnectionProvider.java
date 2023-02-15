package com.cn.xiguaapp.r2dbc.orm.rdb.test.supports.mysql;

import com.cn.xiguaapp.r2dbc.orm.rdb.test.R2dbcConnectionProvider;
import io.r2dbc.spi.Connection;
import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.ConnectionFactoryOptions;
import lombok.SneakyThrows;
import reactor.core.publisher.Mono;

import java.net.URL;

import static io.r2dbc.spi.ConnectionFactoryOptions.*;

/**
 * mysqlR2dbc版连接测试
 */
public class MysqlR2dbcConnectionProvider implements R2dbcConnectionProvider {


    private ConnectionFactory connectionFactory;

    @SneakyThrows
    public MysqlR2dbcConnectionProvider() {

        String username = System.getProperty("mysql.username", "root");
        String password = System.getProperty("mysql.password", "123456.qq");
        String url = System.getProperty("mysql.url", "ip:3306");
        String db = System.getProperty("mysql.db", "demo");

        URL hostUrl = new URL("file://" + url);

        connectionFactory = ConnectionFactories.get(ConnectionFactoryOptions.builder()
                .option(DRIVER, "mysql")
                .option(HOST, hostUrl.getHost())
                .option(PORT, hostUrl.getPort())
                .option(USER, username)
                .option(PASSWORD, password)
                .option(DATABASE, db)
                .build());

    }

    @Override
    public Mono<Connection> getConnection() {
        return Mono.defer(() -> Mono.from(connectionFactory.create()));
    }

    @Override
    public void releaseConnection(Connection connection) {
        connection.close();
    }
}
