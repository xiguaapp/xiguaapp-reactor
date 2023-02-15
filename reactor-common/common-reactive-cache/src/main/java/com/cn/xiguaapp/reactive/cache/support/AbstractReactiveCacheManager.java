package com.cn.xiguaapp.reactive.cache.support;

import com.cn.xiguaapp.reactive.cache.ReactiveCache;
import com.cn.xiguaapp.reactive.cache.ReactiveCacheManager;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author xiguaapp
 * @desc
 * @since 1.0 17:17
 */
public abstract class AbstractReactiveCacheManager implements ReactiveCacheManager {
    private Map<String, ReactiveCache> caches = new ConcurrentHashMap<>();

    @Override
    @SuppressWarnings("all")
    public <E> ReactiveCache<E> getCache(String name) {
        return caches.computeIfAbsent(name, this::createCache);
    }

    protected abstract <E> ReactiveCache<E> createCache(String name);
}