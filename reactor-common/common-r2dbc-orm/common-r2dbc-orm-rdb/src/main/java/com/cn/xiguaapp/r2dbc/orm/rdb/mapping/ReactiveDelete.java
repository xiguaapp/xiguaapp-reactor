package com.cn.xiguaapp.r2dbc.orm.rdb.mapping;


import reactor.core.publisher.Mono;

import java.util.function.BiFunction;

public interface ReactiveDelete extends DSLDelete<ReactiveDelete> {
    Mono<Integer> execute();

    ReactiveDelete onExecute(BiFunction<ReactiveDelete, Mono<Integer>, Mono<Integer>> mapper);
}
