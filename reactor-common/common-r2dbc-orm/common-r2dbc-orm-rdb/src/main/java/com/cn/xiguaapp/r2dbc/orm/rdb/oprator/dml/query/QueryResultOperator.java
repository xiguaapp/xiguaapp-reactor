package com.cn.xiguaapp.r2dbc.orm.rdb.oprator.dml.query;

import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.ResultOperator;
import reactor.core.publisher.Flux;

/**
 * @author xiguaapp
 */
public interface QueryResultOperator<E, R> extends ResultOperator<E, R> {
    @Override
    R sync();

    @Override
    Flux<E> reactive();
}
