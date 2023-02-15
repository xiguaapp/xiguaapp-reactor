package com.cn.xiguaapp.r2dbc.orm.rdb.test.supports.h2;

import com.cn.xiguaapp.r2dbc.orm.rdb.test.ConnectionProvider;
import lombok.SneakyThrows;
import org.h2.Driver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Random;

public class H2ConnectionProvider implements ConnectionProvider {

    private Connection connection;

    public H2ConnectionProvider() {
        Driver.load();
        try {
            connection = DriverManager.getConnection("jdbc:h2:mem:demo" + new Random().nextInt(10000), "sa", "");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @SneakyThrows
    public Connection getConnection() {
        return connection;
    }

    @Override
    @SneakyThrows
    public void releaseConnect(Connection connection) {
        // connection.close();
    }
}
