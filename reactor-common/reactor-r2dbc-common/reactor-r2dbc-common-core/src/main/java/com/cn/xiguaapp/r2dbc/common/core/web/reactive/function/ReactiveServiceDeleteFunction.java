package com.cn.xiguaapp.r2dbc.common.core.web.reactive.function;

import com.cn.xiguapp.common.core.exception.NotFoundException;
import com.cn.xiguaapp.r2dbc.common.core.service.ReactiveCrudService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Function;

/**
 * @author xiguaapp
 * @desc
 * @since 1.0 18:45
 */
public interface ReactiveServiceDeleteFunction<E,K> {
    ReactiveCrudService<E,K>getService();

    /**
     * 根据ID删除
     * @return E
     */
    default Function<Mono<K>,Mono<E>>delete(){
        return id->getService().findById(id).switchIfEmpty(Mono.error(NotFoundException::new))
                .flatMap(e -> getService()
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
