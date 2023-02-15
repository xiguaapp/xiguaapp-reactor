package com.cn.xiguaapp.r2dbc.common.core.web.reactive.function;

/**
 * @author xiguaapp
 * @desc
 * @since 1.0 18:54
 */
public interface ReactiveServiceCrudFunction<E,K> extends ReactiveServiceDeleteFunction<E,K>,ReactiveServiceQueryFunction<E,K>,ReactiveServiceSaveOrUpdateFunction<E,K> {
}
