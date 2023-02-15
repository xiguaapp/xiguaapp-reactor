package com.cn.xiguaapp.r2dbc.common.core.sql;

import com.cn.xiguaapp.datasource.api.core.DataSourceHolder;
import com.cn.xiguaapp.r2dbc.common.api.crud.entity.TransactionManagers;
import com.cn.xiguaapp.r2dbc.orm.rdb.executor.SqlRequest;
import com.cn.xiguaapp.r2dbc.orm.rdb.executor.jdbc.JdbcSyncSqlExecutor;
import com.cn.xiguaapp.r2dbc.orm.rdb.executor.wrapper.ResultWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author xiguaapp
 * @desc jdbc异步sql执行默认执行器 有条件可自己封装
 * @see JdbcSyncSqlExecutor
 * @since 1.0 17:02
 */
@Slf4j
public class DefaultJdbcExecutor extends JdbcSyncSqlExecutor {
    @Autowired
    private DataSource dataSource;
    protected String getDatasourceId() {
        return DataSourceHolder.switcher().datasource().current().orElse("default");
    }

    @Override
    public Connection getConnection(SqlRequest sqlRequest) {

        DataSource dataSource = DataSourceHolder.isDynamicDataSourceReady() ?
                DataSourceHolder.currentDataSource().getNative() :
                this.dataSource;
        Connection connection = DataSourceUtils.getConnection(dataSource);
        boolean isConnectionTransactional = DataSourceUtils.isConnectionTransactional(connection, dataSource);
        if (log.isDebugEnabled()) {
            log.debug("DataSource ({}) JDBC Connection [{}] will {}be managed by Spring", getDatasourceId(), connection, (isConnectionTransactional ? "" : "not "));
        }
        return connection;
    }

    @Override
    public void releaseConnection(Connection connection, SqlRequest sqlRequest) {
        if (log.isDebugEnabled()) {
            log.debug("Releasing DataSource ({}) JDBC Connection [{}]", getDatasourceId(), connection);
        }
        try {
            DataSource dataSource = DataSourceHolder.isDynamicDataSourceReady() ?
                    DataSourceHolder.currentDataSource().getNative() :
                    this.dataSource;
            DataSourceUtils.doReleaseConnection(connection, dataSource);
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
            try {
                connection.close();
            } catch (Exception e2) {
                log.error(e2.getMessage(), e2);
            }
        }
    }

    @Override
    @Transactional(propagation = Propagation.NOT_SUPPORTED, transactionManager = TransactionManagers.jdbcTransactionManager)
    public void execute(SqlRequest request) {
        super.execute(request);
    }

    @Transactional(rollbackFor = Throwable.class, transactionManager = TransactionManagers.jdbcTransactionManager)
    @Override
    public int update(SqlRequest request) {
        return super.update(request);
    }

    @Override
    @Transactional(readOnly = true, transactionManager = TransactionManagers.jdbcTransactionManager)
    public <T, R> R select(SqlRequest request, ResultWrapper<T, R> wrapper) {
        return super.select(request, wrapper);
    }
}
