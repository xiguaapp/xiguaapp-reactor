package com.cn.xiguaapp.reactive.cache.support;

import com.cn.xiguaapp.reactive.cache.ReactiveCache;
import com.cn.xiguaapp.reactive.cache.ReactiveCacheManager;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.redis.core.ReactiveRedisOperations;

/**
 * @author xiguaapp
 * @desc
 * @since 1.0 17:20
 */
public class RedisLocalReactiveCacheManager extends AbstractReactiveCacheManager {

    private ReactiveRedisOperations<Object, Object> operations;

    private ReactiveCacheManager localCacheManager;

    public RedisLocalReactiveCacheManager(ReactiveRedisOperations<Object, Object> operations, ReactiveCacheManager localCacheManager) {
        this.operations = operations;
        this.localCacheManager = localCacheManager;
    }

    @Setter
    @Getter
    private String redisCachePrefix = "spring-cache:";

    @Override
    protected <E> ReactiveCache<E> createCache(String name) {
        return new RedisReactiveCache<>(redisCachePrefix.concat(name), operations, localCacheManager.getCache(name));
    }
}