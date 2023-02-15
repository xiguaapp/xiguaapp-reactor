package com.cn.xiguaapp.r2dbc.orm.rdb.oprator.dml.insert;

import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.ResultOperator;
import reactor.core.publisher.Mono;


/**
 * @author xiguaapp
 */
public interface InsertResultOperator extends ResultOperator<Integer, Integer> {
    @Override
    Integer sync();

    @Override
    Mono<Integer> reactive();
}
