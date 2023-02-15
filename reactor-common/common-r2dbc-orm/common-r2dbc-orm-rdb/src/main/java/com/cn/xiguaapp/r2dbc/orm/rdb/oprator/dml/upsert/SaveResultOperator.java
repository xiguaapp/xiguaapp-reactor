package com.cn.xiguaapp.r2dbc.orm.rdb.oprator.dml.upsert;

import com.cn.xiguaapp.r2dbc.orm.rdb.mapping.defaults.SaveResult;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.ResultOperator;
import reactor.core.publisher.Mono;

public interface SaveResultOperator extends ResultOperator<SaveResult, SaveResult> {
    @Override
    SaveResult sync();

    @Override
    Mono<SaveResult> reactive();
}
