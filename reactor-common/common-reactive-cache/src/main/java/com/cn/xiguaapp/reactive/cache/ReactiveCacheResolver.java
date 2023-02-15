package com.cn.xiguaapp.reactive.cache;

import org.springframework.cache.interceptor.CacheOperationInvocationContext;

import java.util.Collection;

/**
 * @author xiguaapp
 * @desc
 * @since 1.0 17:16
 */
public interface ReactiveCacheResolver {
    Collection<? extends ReactiveCache> resolveCaches(CacheOperationInvocationContext<?> context);
}
