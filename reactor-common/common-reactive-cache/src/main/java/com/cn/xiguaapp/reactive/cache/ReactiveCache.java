package com.cn.xiguaapp.reactive.cache;

import org.reactivestreams.Publisher;
import reactor.cache.CacheFlux;
import reactor.cache.CacheMono;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author xiguaapp
 * @desc 响应式缓存操作接口
 * @since 1.0 17:15
 */
public interface ReactiveCache<E> {
    Flux<E> getFlux(Object key);

    Mono<E> getMono(Object key);

    Mono<Void> put(Object key, Publisher<E> data);

    Mono<Void> evict(Object key);

    Flux<E> getAll(Object... keys);

    Mono<Void> evictAll(Iterable<?> key);

    Mono<Void> clear();

    default CacheFlux.FluxCacheBuilderMapMiss<E> flux(Object key) {
        return otherSupplier ->
                Flux.defer(() ->
                        getFlux(key)
                                .switchIfEmpty(otherSupplier.get()
                                        .collectList()
                                        .flatMapMany(values -> put(key, Flux.fromIterable(values))
                                                .thenMany(Flux.fromIterable(values)))));
    }

    default CacheMono.MonoCacheBuilderMapMiss<E> mono(Object key) {
        return otherSupplier ->
                Mono.defer(() -> getMono(key)
                        .switchIfEmpty(otherSupplier.get()
                                .flatMap(value -> put(key, Mono.just(value)).thenReturn(value))));
    }
}
