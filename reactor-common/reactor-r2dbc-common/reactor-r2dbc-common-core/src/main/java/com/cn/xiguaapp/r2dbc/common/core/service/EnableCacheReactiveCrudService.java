package com.cn.xiguaapp.r2dbc.common.core.service;

import com.cn.xiguaapp.r2dbc.orm.rdb.mapping.ReactiveDelete;
import com.cn.xiguaapp.r2dbc.orm.rdb.mapping.ReactiveUpdate;
import com.cn.xiguaapp.r2dbc.orm.rdb.mapping.defaults.SaveResult;
import com.cn.xiguaapp.reactive.cache.ReactiveCache;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collection;

/**
 * @author xiguaapp
 * @desc 带缓存的增删改查方法 详情查看 {@link ReactiveCrudService}
 * @since 1.0 16:58
 */
public interface EnableCacheReactiveCrudService<E, K> extends ReactiveCrudService<E, K> {
    ReactiveCache<E> getCache();

    /**
     * 根据id获取数据
     * @param id id
     * @return 响应式数据流
     */
    @Override
    default Mono<E> findById(K id) {
        return this.getCache()
                .mono("id:" + id)
                .onCacheMissResume(ReactiveCrudService.super.findById(Mono.just(id)));
    }

    /**
     * 根据id查询数据(多id)
     * @param publisher id类
     * @return 响应式数据流
     */
    @Override
    default Mono<E> findById(Mono<K> publisher) {
        return publisher.flatMap(this::findById);
    }

    /**
     * 根据id修改数据
     * @param id id
     * @param entityPublisher 实体类
     * @return 数值
     */
    @Override
    default Mono<Integer> updateById(K id, Mono<E> entityPublisher) {
        return ReactiveCrudService.super.updateById(id, entityPublisher)
                .doFinally(i -> getCache().evict("id:" + id).subscribe());
    }

    /**
     * 保存
     * @param entityPublisher 实体
     * @return SaveResult结构数据
     */
    @Override
    default Mono<SaveResult> save(Publisher<E> entityPublisher) {
        return ReactiveCrudService.super.save(entityPublisher)
                .doFinally(i -> getCache().clear().subscribe());
    }

    /**
     * 保存
     * @param entityPublisher 实体
     * @return 数字
     */
    @Override
    default Mono<Integer> insert(Publisher<E> entityPublisher) {
        return ReactiveCrudService.super.insert(entityPublisher)
                .doFinally(i -> getCache().clear().subscribe());
    }

    /**
     * 批量保存
     * @param entityPublisher 实体集合
     * @return 数据
     */
    @Override
    default Mono<Integer> insertBatch(Publisher<? extends Collection<E>> entityPublisher) {
        return ReactiveCrudService.super.insertBatch(entityPublisher)
                .doFinally(i -> getCache().clear().subscribe());
    }

    /**
     * 根据id删除数据(可批量)
     * @param idPublisher id
     * @return 数字
     */
    @Override
    default Mono<Integer> deleteById(Publisher<K> idPublisher) {
        return Flux.from(idPublisher)
                .doOnNext(id -> this.getCache().evict("id:" + id).subscribe())
                .as(ReactiveCrudService.super::deleteById);
    }

    /**
     * 保存和更新
     * @return 实体
     */
    @Override
    default ReactiveUpdate<E> createUpdate() {
        return ReactiveCrudService.super.createUpdate()
                .onExecute((update, s) -> s.doFinally((__) -> getCache().clear().subscribe()));
    }

    /**
     * 动态删除
     * @return
     */
    @Override
    default ReactiveDelete createDelete() {
        return ReactiveCrudService.super.createDelete()
                .onExecute((update, s) -> s.doFinally((__) -> getCache().clear().subscribe()));
    }
}
