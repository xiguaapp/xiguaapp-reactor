package com.cn.xiguaapp.datasource.api.core;

import io.r2dbc.spi.ConnectionFactory;
import reactor.core.publisher.Mono;

/**
 * @author xiguaapp
 */
public interface R2dbcDataSource extends DynamicDataSource<Mono<ConnectionFactory>> {
    @Override
    Mono<ConnectionFactory> getNative();
}
