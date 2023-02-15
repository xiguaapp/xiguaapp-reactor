package com.cn.xiguaapp.reactive.cache.support;

import com.cn.xiguaapp.reactive.cache.ReactiveCache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.AllArgsConstructor;

/**
 * @author xiguaapp
 * @desc
 * @since 1.0 17:18
 */
@AllArgsConstructor
public class CaffeineReactiveCacheManager extends AbstractReactiveCacheManager {

    private Caffeine<Object, Object> builder;


    @Override
    protected <E> ReactiveCache<E> createCache(String name) {
        return new CaffeineReactiveCache<>(builder.build());
    }

}
