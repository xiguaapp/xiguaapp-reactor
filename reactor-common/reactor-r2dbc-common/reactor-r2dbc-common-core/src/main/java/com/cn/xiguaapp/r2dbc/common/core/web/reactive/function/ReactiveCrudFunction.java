package com.cn.xiguaapp.r2dbc.common.core.web.reactive.function;

/**
 * @author xiguaapp
 * @desc
 * @since 1.0 18:47
 */
public interface ReactiveCrudFunction<E,K> extends ReactiveQueryFunction<E,K>,ReactiveDeleteFunction<E,K>,ReactiveSaveOrUpdateFunction<E,K> {
}
