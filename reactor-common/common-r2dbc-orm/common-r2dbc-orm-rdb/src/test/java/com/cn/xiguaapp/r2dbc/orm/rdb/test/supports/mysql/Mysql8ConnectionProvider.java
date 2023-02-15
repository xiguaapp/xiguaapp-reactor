package com.cn.xiguaapp.r2dbc.orm.rdb.test.supports.mysql;

import com.cn.xiguaapp.r2dbc.orm.rdb.executor.SqlRequests;
import com.cn.xiguaapp.r2dbc.orm.rdb.executor.SyncSqlExecutor;
import com.cn.xiguaapp.r2dbc.orm.rdb.test.ConnectionProvider;
import com.cn.xiguaapp.r2dbc.orm.rdb.test.TestSyncSqlExecutor;
import com.mysql.jdbc.Driver;
import lombok.SneakyThrows;

import java.sql.Connection;
import java.sql.DriverManager;

import static com.cn.xiguaapp.r2dbc.orm.rdb.executor.wrapper.ResultWrappers.mapList;

/**
 * mysql8链接测试
 */
public class Mysql8ConnectionProvider implements ConnectionProvider {

    static {
        Driver.getPlatform();
    }

    @SneakyThrows
    public Connection getConnection() {

        String username = System.getProperty("mysql8.username", "root");
        String password = System.getProperty("mysql8.password", "123456.qq");
        String url = System.getProperty("mysql8.url", "ip");
        String db = System.getProperty("mysql8.db", "demo");
        return DriverManager.getConnection("jdbc:mysql://" + url + "/" + db + "?allowPublicKeyRetrieval=true&useSSL=false", username, password);

    }

    @Override
    @SneakyThrows
    public void releaseConnect(Connection connection) {
        connection.close();
    }

    public static void main(String[] args) {
        SyncSqlExecutor executor  =new TestSyncSqlExecutor(new Mysql8ConnectionProvider());;
        System.out.println(executor.select(SqlRequests.of("show index from demo.user"),mapList()));

    }
}
