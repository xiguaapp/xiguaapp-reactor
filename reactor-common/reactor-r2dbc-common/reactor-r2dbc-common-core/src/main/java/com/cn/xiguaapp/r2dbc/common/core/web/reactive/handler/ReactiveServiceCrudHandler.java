package com.cn.xiguaapp.r2dbc.common.core.web.reactive.handler;

import com.cn.xiguaapp.r2dbc.common.core.web.reactive.function.ReactiveServiceCrudFunction;
import com.cn.xiguaapp.r2dbc.common.core.web.reactive.function.ReactiveServiceDeleteFunction;
import com.cn.xiguaapp.r2dbc.common.core.web.reactive.function.ReactiveServiceQueryFunction;
import com.cn.xiguaapp.r2dbc.common.core.web.reactive.function.ReactiveServiceSaveOrUpdateFunction;

/**
 * @author xiguaapp
 * @desc service Crud集合
 * @since 1.0 19:56
 */
public interface ReactiveServiceCrudHandler<T,ID> extends ReactiveServiceQueryHandler<T,ID>,ReactiveServiceSaveOrUpdateHandler<T,ID>,ReactiveServiceDeleteHandler<T,ID> {
    ReactiveServiceCrudFunction<T,ID> getReactiveServiceCrudFunction();
    @Override
    default ReactiveServiceDeleteFunction<T,ID> getReactiveServiceDeleteFunction() {
        return getReactiveServiceCrudFunction();
    }

    @Override
    default ReactiveServiceQueryFunction<T,ID> getReactiveServiceQueryFunction() {
        return getReactiveServiceCrudFunction();
    }

    @Override
    default ReactiveServiceSaveOrUpdateFunction<T,ID> getServiceSaveOrUpdateFunction() {
        return getReactiveServiceCrudFunction();
    }
}
