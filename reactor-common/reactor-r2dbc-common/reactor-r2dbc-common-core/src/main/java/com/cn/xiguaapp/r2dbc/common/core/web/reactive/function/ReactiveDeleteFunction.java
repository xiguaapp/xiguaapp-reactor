package com.cn.xiguaapp.r2dbc.common.core.web.reactive.function;

import com.cn.xiguapp.common.core.exception.NotFoundException;
import com.cn.xiguaapp.r2dbc.orm.rdb.mapping.ReactiveRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Function;

/**
 * @author xiguaapp
 * @desc
 * @since 1.0 18:39
 */
public interface ReactiveDeleteFunction<E,K> {
    ReactiveRepository<E,K>getRepository();

    /**
     * 根据ID删除
     * @return E
     */
    default Function<Mono<K>, Mono<E>>delete(){
        return id->getRepository()
                .findById(id)
                .switchIfEmpty(Mono.error(NotFoundException::new))
                .flatMap(e->getRepository()
                        .deleteById(id)
                        .thenReturn(e));
    }

    /**
     * 批量删除
     * @return E
     */
    default Function<Flux<K>,Flux<E>>deleteBatch(){
        return ids->ids.flatMap(id->delete().apply(Mono.just(id)));
    }
}
