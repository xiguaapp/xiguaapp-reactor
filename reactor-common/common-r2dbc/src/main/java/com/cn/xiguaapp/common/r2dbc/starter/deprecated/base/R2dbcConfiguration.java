package com.cn.xiguaapp.common.r2dbc.starter.deprecated.base;

import io.r2dbc.spi.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;
import org.springframework.data.r2dbc.connectionfactory.R2dbcTransactionManager;
import org.springframework.data.r2dbc.convert.R2dbcCustomConversions;
import org.springframework.transaction.ReactiveTransactionManager;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xiguaapp
 * @desc 配置类
 * @since 1.0 16:48
 */
@Slf4j
@Deprecated
public abstract class R2dbcConfiguration<T> extends AbstractR2dbcConfiguration {

    private final ConnectionFactory factory;

    protected R2dbcConfiguration(@Qualifier("connectionFactory")ConnectionFactory factory) {
        this.factory = factory;
    }

    @Override
    @SuppressWarnings("uncheck")
    public ConnectionFactory connectionFactory() {
        return factory;
    }
    /**
     * 本地事务处理
     * @return
     */
    @Bean
    ReactiveTransactionManager transactionManager(){
        return new R2dbcTransactionManager(this.factory);
    }

    @Override
    public R2dbcCustomConversions r2dbcCustomConversions() {
        List<Converter<?,?>> converterList = new ArrayList<>();
        converterList.add(new BaseConvert<>());
        return new R2dbcCustomConversions(getStoreConversions(), converterList);
    }
}