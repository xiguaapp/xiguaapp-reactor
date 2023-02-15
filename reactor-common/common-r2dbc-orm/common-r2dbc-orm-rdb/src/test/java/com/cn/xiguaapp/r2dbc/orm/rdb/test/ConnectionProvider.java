package com.cn.xiguaapp.r2dbc.orm.rdb.test;

import java.sql.Connection;

public interface ConnectionProvider {

    Connection getConnection();

    void releaseConnect(Connection connection);
}
