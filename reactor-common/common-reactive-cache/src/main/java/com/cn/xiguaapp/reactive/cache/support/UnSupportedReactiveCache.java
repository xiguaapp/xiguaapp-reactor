package com.cn.xiguaapp.reactive.cache.support;

import com.cn.xiguaapp.reactive.cache.ReactiveCache;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.reactivestreams.Publisher;
import reactor.cache.CacheFlux;
import reactor.cache.CacheMono;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Supplier;

/**
 * @author xiguaapp
 * @desc
 * @since 1.0 17:22
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UnSupportedReactiveCache <E> implements ReactiveCache<E> {

    private static final UnSupportedReactiveCache INSTANCE = new UnSupportedReactiveCache();

    public static <E> ReactiveCache<E> getInstance() {
        return INSTANCE;
    }

    @Override
    public Flux<E> getFlux(Object key) {
        return Flux.empty();
    }

    @Override
    public Mono<E> getMono(Object key) {
        return Mono.empty();
    }

    @Override
    public Mono<Void> put(Object key, Publisher<E> data) {
        return Mono.empty();
    }

    @Override
    public Mono<Void> evict(Object key) {
        return Mono.empty();
    }

    @Override
    public Mono<Void> evictAll(Iterable<?> key) {
        return Mono.empty();
    }

    @Override
    public Flux<E> getAll(Object... keys) {
        return Flux.empty();
    }

    @Override
    public Mono<Void> clear() {
        return Mono.empty();
    }

    @Override
    public CacheMono.MonoCacheBuilderMapMiss<E> mono(Object key) {
        return Supplier::get;
    }

    @Override
    public CacheFlux.FluxCacheBuilderMapMiss<E> flux(Object key) {
        return Supplier::get;
    }
}