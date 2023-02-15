package com.cn.xiguaapp.r2dbc.orm.rdb.test.supports.oracle;

import com.cn.xiguaapp.r2dbc.orm.rdb.test.ConnectionProvider;
import lombok.SneakyThrows;

import java.sql.Connection;
import java.sql.DriverManager;

public class OracleConnectionProvider implements ConnectionProvider {

    static {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @SneakyThrows
    public Connection getConnection() {

        String username = System.getProperty("oracle.username", "system");
        String password = System.getProperty("oracle.password", "oracle");
        String url = System.getProperty("oracle.url", "127.0.0.1:11521");
        String db = System.getProperty("oracle.db", "orcl");
        return DriverManager.getConnection("jdbc:oracle:thin:@" + url + ":" + db, username, password);

    }

    @Override
    @SneakyThrows
    public void releaseConnect(Connection connection) {
        connection.close();
    }
}
