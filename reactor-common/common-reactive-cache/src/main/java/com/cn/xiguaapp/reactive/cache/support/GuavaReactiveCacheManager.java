package com.cn.xiguaapp.reactive.cache.support;

import com.cn.xiguaapp.reactive.cache.ReactiveCache;
import com.google.common.cache.CacheBuilder;
import lombok.AllArgsConstructor;

/**
 * @author xiguaapp
 * @desc
 * @since 1.0 17:20
 */
@AllArgsConstructor
public class GuavaReactiveCacheManager extends AbstractReactiveCacheManager {

    private CacheBuilder<Object, Object> builder;

    @Override
    protected <E> ReactiveCache<E> createCache(String name) {
        return new GuavaReactiveCache<>(builder.build());
    }

}
