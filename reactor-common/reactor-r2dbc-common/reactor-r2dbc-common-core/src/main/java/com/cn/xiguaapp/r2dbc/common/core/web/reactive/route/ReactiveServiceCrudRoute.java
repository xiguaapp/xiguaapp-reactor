package com.cn.xiguaapp.r2dbc.common.core.web.reactive.route;

import com.cn.xiguaapp.r2dbc.common.core.web.reactive.handler.ReactiveServiceCrudHandler;
import com.cn.xiguaapp.r2dbc.common.core.web.reactive.handler.ReactiveServiceDeleteHandler;
import com.cn.xiguaapp.r2dbc.common.core.web.reactive.handler.ReactiveServiceQueryHandler;
import com.cn.xiguaapp.r2dbc.common.core.web.reactive.handler.ReactiveServiceSaveOrUpdateHandler;

/**
 * @author xiguaapp
 * @desc
 * @since 1.0 21:26
 */
public interface ReactiveServiceCrudRoute<T,ID> extends ReactiveServiceDeleteRoute<T,ID>,ReactiveServiceQueryRoute<T,ID>,ReactiveServiceSaveOrUpdateRoute<T,ID> {
    ReactiveServiceCrudHandler<T,ID> getReactiveServiceCrudHandler();
    @Override
    default ReactiveServiceDeleteHandler<T,ID> getReactiveServiceDeleteHandler() {
        return getReactiveServiceCrudHandler();
    }

    @Override
    default ReactiveServiceQueryHandler<T,ID> getReactiveServiceQueryHandler() {
        return getReactiveServiceCrudHandler();
    }

    @Override
    default ReactiveServiceSaveOrUpdateHandler<T,ID> getReactiveServiceSaveOrUpdateHandler() {
        return getReactiveServiceCrudHandler();
    }
}
