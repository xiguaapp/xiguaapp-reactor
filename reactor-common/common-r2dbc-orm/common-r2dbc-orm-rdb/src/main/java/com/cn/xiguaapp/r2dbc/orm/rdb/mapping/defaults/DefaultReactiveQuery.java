package com.cn.xiguaapp.r2dbc.orm.rdb.mapping.defaults;


import com.cn.xiguaapp.r2dbc.orm.rdb.event.ContextKeyValue;
import com.cn.xiguaapp.r2dbc.orm.rdb.event.ContextKeys;
import com.cn.xiguaapp.r2dbc.orm.rdb.executor.wrapper.ResultWrapper;
import com.cn.xiguaapp.r2dbc.orm.rdb.mapping.EntityColumnMapping;
import com.cn.xiguaapp.r2dbc.orm.rdb.mapping.ReactiveQuery;
import com.cn.xiguaapp.r2dbc.orm.rdb.mapping.events.MappingEventTypes;
import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.TableOrViewMetadata;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.DMLOperator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.cn.xiguaapp.r2dbc.orm.rdb.event.ContextKeys.source;
import static com.cn.xiguaapp.r2dbc.orm.rdb.executor.wrapper.ResultWrappers.column;
import static com.cn.xiguaapp.r2dbc.orm.rdb.mapping.events.EventSupportWrapper.eventWrapper;
import static com.cn.xiguaapp.r2dbc.orm.rdb.mapping.events.MappingContextKeys.*;
import static com.cn.xiguaapp.r2dbc.orm.rdb.oprator.dml.query.Selects.count1;

/**
 * @author xiguaapp
 */
public class DefaultReactiveQuery<T> extends DefaultQuery<T, ReactiveQuery<T>> implements ReactiveQuery<T> {

    public DefaultReactiveQuery(TableOrViewMetadata tableMetadata,
                                EntityColumnMapping mapping,
                                DMLOperator operator,
                                ResultWrapper<T, ?> wrapper,
                                ContextKeyValue<?>... keyValues) {
        super(tableMetadata, mapping, operator, wrapper,keyValues);
    }

    @Override
    public Flux<T> fetch() {
        return operator
                .query(tableMetadata)
                .select(getSelectColumn())
                .where(param.getTerms())
                .orderBy(getSortOrder())
                .when(param.isPaging(), query -> query.paging(param.getPageIndex(), param.getPageSize()))
                .accept(queryOperator ->
                        tableMetadata.fireEvent(MappingEventTypes.select_before, eventContext ->
                                eventContext.set(
                                        source(DefaultReactiveQuery.this),
                                        query(queryOperator),
                                        ContextKeys.tableMetadata(tableMetadata),
                                        dml(operator),
                                        executorType("reactive"),
                                        type("fetch")
                                )))
                .fetch(eventWrapper(tableMetadata, wrapper, executorType("reactive"), type("fetch")))
                .reactive();
    }

    @Override
    public Mono<T> fetchOne() {
        return  operator
                .query(tableMetadata)
                .select(getSelectColumn())
                .where(param.getTerms())
                .orderBy(getSortOrder())
                .paging(0, 1)
                .accept(queryOperator ->
                        tableMetadata.fireEvent(MappingEventTypes.select_before, eventContext ->
                                eventContext.set(
                                        source(DefaultReactiveQuery.this),
                                        query(queryOperator),
                                        dml(operator),
                                        ContextKeys.tableMetadata(tableMetadata),
                                        executorType("reactive"),
                                        type("fetchOne")
                                )))
                .fetch(eventWrapper(tableMetadata, wrapper, executorType("reactive"), type("fetchOne")))
                .reactive()
                .as(Mono::from);
    }

    @Override
    public Mono<Integer> count() {
        return operator
                .query(tableMetadata)
                .select(count1().as("total"))
                .where(param.getTerms())
                .accept(queryOperator ->
                        tableMetadata.fireEvent(MappingEventTypes.select_before, eventContext ->
                                eventContext.set(
                                        source(DefaultReactiveQuery.this),
                                        query(queryOperator),
                                        dml(operator),
                                        ContextKeys.tableMetadata(tableMetadata),
                                        executorType("reactive"),
                                        type("count")
                                )))
                .fetch(column("total", Number.class::cast))
                .reactive()
                .map(Number::intValue)
                .reduce(Math::addExact)
                .switchIfEmpty(Mono.just(0));
    }

}
