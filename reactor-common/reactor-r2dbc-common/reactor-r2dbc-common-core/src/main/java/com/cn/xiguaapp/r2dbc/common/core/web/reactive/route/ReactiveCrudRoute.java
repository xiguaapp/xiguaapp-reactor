package com.cn.xiguaapp.r2dbc.common.core.web.reactive.route;

import com.cn.xiguaapp.r2dbc.common.core.web.reactive.handler.ReactiveCrudHandler;
import com.cn.xiguaapp.r2dbc.common.core.web.reactive.handler.ReactiveDeleteHandler;
import com.cn.xiguaapp.r2dbc.common.core.web.reactive.handler.ReactiveQueryHandler;
import com.cn.xiguaapp.r2dbc.common.core.web.reactive.handler.ReactiveSaveOrUpdateHandler;

/**
 * @author xiguaapp
 * @desc 增删改查route集合
 * @since 1.0 21:25
 */
public interface ReactiveCrudRoute<T,ID> extends ReactiveDeleteRoute<T,ID>,ReactiveQueryRoute<T,ID>,ReactiveSaveOrUpdateRoute<T,ID> {
    ReactiveCrudHandler<T,ID>getReactiveCrudHandler();
    @Override
    default ReactiveDeleteHandler<T,ID> getReactiveDeleteHandler() {
        return getReactiveCrudHandler();
    }

    @Override
    default ReactiveQueryHandler<T,ID> getReactiveQueryHandler() {
        return getReactiveCrudHandler();
    }

    @Override
    default ReactiveSaveOrUpdateHandler<T,ID> getReactiveSaveOrUpdateHandler() {
        return getReactiveCrudHandler();
    }
}
