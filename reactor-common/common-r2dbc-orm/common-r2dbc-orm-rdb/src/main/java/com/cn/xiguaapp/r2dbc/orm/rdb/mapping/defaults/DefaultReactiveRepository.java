package com.cn.xiguaapp.r2dbc.orm.rdb.mapping.defaults;


import com.cn.xiguaapp.r2dbc.orm.rdb.executor.wrapper.ResultWrapper;
import com.cn.xiguaapp.r2dbc.orm.rdb.mapping.ReactiveDelete;
import com.cn.xiguaapp.r2dbc.orm.rdb.mapping.ReactiveQuery;
import com.cn.xiguaapp.r2dbc.orm.rdb.mapping.ReactiveRepository;
import com.cn.xiguaapp.r2dbc.orm.rdb.mapping.ReactiveUpdate;
import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.RDBTableMetadata;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.DatabaseOperator;
import org.apache.commons.collections.CollectionUtils;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.function.Supplier;

/**
 * @author xiguaapp
 */
public class DefaultReactiveRepository<E, K> extends DefaultRepository<E> implements ReactiveRepository<E, K> {

    public DefaultReactiveRepository(DatabaseOperator operator, String table, Class<E> type, ResultWrapper<E, ?> wrapper) {
        this(operator,
                () -> operator.getMetadata().getTable(table).orElseThrow(() -> new UnsupportedOperationException("table [" + table + "] doesn't exist")), type, wrapper);
    }

    public DefaultReactiveRepository(DatabaseOperator operator, RDBTableMetadata table, Class<E> type, ResultWrapper<E, ?> wrapper) {
        this(operator, () -> table, type, wrapper);
    }

    public DefaultReactiveRepository(DatabaseOperator operator, Supplier<RDBTableMetadata> table, Class<E> type, ResultWrapper<E, ?> wrapper) {
        super(operator, table, wrapper);
        initMapping(type);
    }

    @Override
    public Mono<E> newInstance() {
        return Mono.just(wrapper.newRowInstance());
    }

    @Override
    public Mono<E> findById(Mono<K> primaryKey) {
        return primaryKey
                .flatMap(k -> createQuery().where(getIdColumn(), k).fetchOne());
    }

    @Override
    public Flux<E> findById(Flux<K> key) {
        return key.collectList()
                .filter(CollectionUtils::isNotEmpty)
                .flatMapMany(idList -> createQuery().where().in(getIdColumn(), idList).fetch());
    }

    @Override
    public Mono<Integer> deleteById(Publisher<K> key) {
        return Flux.from(key)
                .collectList()
                .filter(CollectionUtils::isNotEmpty)
                .flatMap(list -> createDelete().where().in(getIdColumn(), list).execute())
                .defaultIfEmpty(0);
    }

    @Override
    public Mono<Integer> updateById(K id, Mono<E> data) {
        return data
                .flatMap(_data -> createUpdate()
                        .where(getIdColumn(), id)
                        .set(_data)
                        .execute());
    }

    @Override
    public Mono<SaveResult> save(Publisher<E> data) {
        return Flux
                .from(data)
                .collectList()
                .filter(CollectionUtils::isNotEmpty)
                .flatMap(list -> doSave(list).reactive())
                .defaultIfEmpty(SaveResult.of(0, 0));
    }

    @Override
    public Mono<Integer> insert(Publisher<E> data) {
        return Flux.from(data)
                .flatMap(e -> doInsert(e).reactive().flux())
                .reduce(Math::addExact)
                .defaultIfEmpty(0);
    }

    @Override
    public Mono<Integer> insertBatch(Publisher<? extends Collection<E>> data) {
        return Flux.from(data)
                .filter(CollectionUtils::isNotEmpty)
                .flatMap(e -> doInsert(e).reactive().flux())
                .reduce(Math::addExact)
                .defaultIfEmpty(0);
    }

    @Override
    public ReactiveQuery<E> createQuery() {
        return new DefaultReactiveQuery<>(getTable()
                , mapping
                , operator.dml()
                , wrapper
                , getDefaultContextKeyValue());
    }

    @Override
    public ReactiveUpdate<E> createUpdate() {
        return new DefaultReactiveUpdate<>(getTable()
                , operator.dml().update(getTable().getFullName())
                , mapping, getDefaultContextKeyValue());
    }

    @Override
    public ReactiveDelete createDelete() {
        return new DefaultReactiveDelete(getTable()
                , operator.dml().delete(getTable().getFullName())
                , getDefaultContextKeyValue()
        );
    }
}
