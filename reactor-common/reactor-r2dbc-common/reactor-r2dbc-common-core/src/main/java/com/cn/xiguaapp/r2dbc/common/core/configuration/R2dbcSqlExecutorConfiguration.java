package com.cn.xiguaapp.r2dbc.common.core.configuration;

import com.cn.xiguaapp.r2dbc.common.core.sql.DefaultR2dbcExecutor;
import com.cn.xiguaapp.r2dbc.orm.rdb.executor.SyncSqlExecutor;
import com.cn.xiguaapp.r2dbc.orm.rdb.executor.reactive.ReactiveSqlExecutor;
import com.cn.xiguaapp.r2dbc.orm.rdb.executor.reactive.ReactiveSyncSqlExecutor;
import io.r2dbc.spi.ConnectionFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author xiguaapp
 * @desc
 * @since 1.0 15:17
 */
@Configuration
@AutoConfigureAfter(name = "org.springframework.boot.autoconfigure.r2dbc.R2dbcAutoConfiguration")
@ConditionalOnBean(ConnectionFactory.class)
public class R2dbcSqlExecutorConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public ReactiveSqlExecutor reactiveSqlExecutor(EasyormProperties properties) {
        DefaultR2dbcExecutor executor = new DefaultR2dbcExecutor();
        executor.setBindSymbol(properties.getDialect().getBindSymbol());
        executor.setBindCustomSymbol(!executor.getBindSymbol().equals("?"));
        return executor;
    }

    @Bean
    @ConditionalOnMissingBean
    public SyncSqlExecutor syncSqlExecutor(ReactiveSqlExecutor reactiveSqlExecutor) {
        return ReactiveSyncSqlExecutor.of(reactiveSqlExecutor);
    }
}
