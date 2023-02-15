package com.cn.xiguaapp.r2dbc.common.api.crud.entity;

/**
 * @author xiguaapp
 * @desc 事务接口 支持r2dbc和jdbc
 * @since 1.0 22:34
 */
public interface TransactionManagers {
    String r2dbcTransactionManager = "connectionFactoryTransactionManager";// System.getProperty("");

    String jdbcTransactionManager = "transactionManager";
}
