package com.cn.xiguaapp.r2dbc.orm.rdb.oprator;


import com.cn.xiguaapp.r2dbc.orm.rdb.executor.SyncSqlExecutor;
import com.cn.xiguaapp.r2dbc.orm.rdb.executor.reactive.ReactiveSqlExecutor;

/**
 * @author xiguaapp
 */
public interface SQLOperator {

    SyncSqlExecutor sync();

    ReactiveSqlExecutor reactive();

}
