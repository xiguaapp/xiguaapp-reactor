package com.cn.xiguaapp.r2dbc.common.core.service;

import com.cn.xiguaapp.r2dbc.orm.rdb.mapping.ReactiveRepository;
import com.cn.xiguaapp.reactive.cache.ReactiveCache;
import com.cn.xiguaapp.reactive.cache.ReactiveCacheManager;
import com.cn.xiguaapp.reactive.cache.support.UnSupportedReactiveCache;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author xiguaapp
 * @desc 响应式带缓存的增删改查方法 详情查看 {@link ReactiveCrudService}
 * @since 1.0 17:31
 */
public abstract class GenericReactiveCacheSupportCrudService<E, K> implements EnableCacheReactiveCrudService<E, K>{
    @Autowired
    private ReactiveRepository<E, K> repository;

    @Override
    public ReactiveRepository<E, K> getRepository() {
        return repository;
    }

    @Autowired(required = false)
    private ReactiveCacheManager cacheManager;

    protected ReactiveCache<E> cache;

    @Override
    public ReactiveCache<E> getCache() {
        if (cache != null) {
            return cache;
        }
        if (cacheManager == null) {
            return cache = UnSupportedReactiveCache.getInstance();
        }

        return cache = cacheManager.getCache(getCacheName());
    }

    public String getCacheName() {
        return this.getClass().getSimpleName();
    }
}
