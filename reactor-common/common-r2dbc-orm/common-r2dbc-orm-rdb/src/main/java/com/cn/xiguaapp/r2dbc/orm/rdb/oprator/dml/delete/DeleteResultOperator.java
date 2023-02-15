package com.cn.xiguaapp.r2dbc.orm.rdb.oprator.dml.delete;

import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.ResultOperator;
import reactor.core.publisher.Mono;


/**
 * @author xiguaapp
 */
public interface DeleteResultOperator extends ResultOperator<Integer, Integer> {
    @Override
    Integer sync();

    @Override
    Mono<Integer> reactive();
}
