package com.cn.xiguaapp.r2dbc.common.core.web.reactive.handler;

import com.cn.xiguaapp.r2dbc.common.core.web.reactive.function.ReactiveCrudFunction;
import com.cn.xiguaapp.r2dbc.common.core.web.reactive.function.ReactiveDeleteFunction;
import com.cn.xiguaapp.r2dbc.common.core.web.reactive.function.ReactiveQueryFunction;
import com.cn.xiguaapp.r2dbc.common.core.web.reactive.function.ReactiveSaveOrUpdateFunction;

/**
 * @author xiguaapp
 * @desc
 * @since 1.0 19:55
 */
public interface ReactiveCrudHandler<T,ID> extends ReactiveQueryHandler<T,ID>,ReactiveSaveOrUpdateHandler<T,ID>,ReactiveDeleteHandler<T,ID> {
    ReactiveCrudFunction<T,ID> getReactiveCrudFunction();

    @Override
    default ReactiveDeleteFunction<T,ID> getReactiveDeleteFunction() {
        return getReactiveCrudFunction();
    }

    @Override
    default ReactiveQueryFunction<T,ID> getReactiveQueryFunction() {
        return getReactiveCrudFunction();
    }

    @Override
    default ReactiveSaveOrUpdateFunction<T,ID> getReactiveSaveOrUpdateFunction() {
        return getReactiveCrudFunction();
    }
}
