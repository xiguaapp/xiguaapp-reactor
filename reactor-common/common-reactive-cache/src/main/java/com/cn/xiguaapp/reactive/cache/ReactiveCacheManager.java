package com.cn.xiguaapp.reactive.cache;

/**
 * @author xiguaapp
 * @desc
 * @since 1.0 17:16
 */
@FunctionalInterface
public interface ReactiveCacheManager {
    <E> ReactiveCache<E> getCache(String name);
}
