package com.cn.xiguaapp.common.r2dbc.starter.deprecated.config;

import io.r2dbc.spi.ConnectionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;
import org.springframework.data.r2dbc.connectionfactory.R2dbcTransactionManager;
import org.springframework.transaction.ReactiveTransactionManager;

/**
 * @author xiguaapp
 * @Date 2020/9/9
 * @desc 新版本后即将移除
 */
@Deprecated
public abstract class SuperR2dbcConfig extends AbstractR2dbcConfiguration {
    private final ConnectionFactory factory;
    public SuperR2dbcConfig(@Qualifier("connectionFactory") ConnectionFactory factory) {
        this.factory = factory;
    }

    @Override
    public ConnectionFactory connectionFactory() {
        return factory;
    }
    /**
     * 本地事务处理
     * @param factory 连接
     * @return
     */
    @Bean
    ReactiveTransactionManager transactionManager(@Qualifier("connectionFactory") ConnectionFactory factory){
        return new R2dbcTransactionManager(factory);
    }

}