package com.cn.xiguaapp.r2dbc.common.core.sql;

import com.cn.xiguaapp.datasource.api.core.DataSourceHolder;
import com.cn.xiguaapp.r2dbc.orm.rdb.executor.SqlRequest;
import com.cn.xiguaapp.r2dbc.orm.rdb.executor.jdbc.JdbcReactiveSqlExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceUtils;
import reactor.core.publisher.Mono;

import javax.sql.DataSource;
import java.sql.Connection;

/**
 * @author xiguaapp
 * @desc jdbc响应式reactive默认执行器 有条件可自行封装
 * @see JdbcReactiveSqlExecutor
 * @since 1.0 17:06
 */
public class DefaultJdbcReactiveExecutor extends JdbcReactiveSqlExecutor {
    @Autowired
    private DataSource dataSource;

    @Override
    public Mono<Connection> getConnection(SqlRequest sqlRequest) {

        DataSource dataSource = DataSourceHolder.isDynamicDataSourceReady() ?
                DataSourceHolder.currentDataSource().getNative() :
                this.dataSource;
        Connection connection = DataSourceUtils.getConnection(dataSource);
        return Mono.just(connection);

    }

    @Override
    public void releaseConnection(Connection connection, SqlRequest sqlRequest) {
        DataSource dataSource = DataSourceHolder.isDynamicDataSourceReady() ?
                DataSourceHolder.currentDataSource().getNative() :
                this.dataSource;
        DataSourceUtils.releaseConnection(connection, dataSource);
    }
}
