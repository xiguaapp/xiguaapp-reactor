package com.cn.xiguaapp.r2dbc.orm.rdb.oprator.dml.update;

import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.ResultOperator;
import reactor.core.publisher.Mono;


public interface UpdateResultOperator extends ResultOperator<Integer, Integer> {
    @Override
    Integer sync();

    @Override
    Mono<Integer> reactive();
}
