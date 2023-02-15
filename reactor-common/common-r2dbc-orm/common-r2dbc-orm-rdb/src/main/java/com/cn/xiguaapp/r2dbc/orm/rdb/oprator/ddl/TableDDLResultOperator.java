package com.cn.xiguaapp.r2dbc.orm.rdb.oprator.ddl;

import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.ResultOperator;
import reactor.core.publisher.Mono;

/**
 * @author xiguaapp
 */
public interface TableDDLResultOperator extends ResultOperator<Boolean, Boolean> {

    @Override
    Boolean sync();

    @Override
    Mono<Boolean> reactive();

}
