package com.cn.xiguaapp.r2dbc.orm.rdb.mapping.defaults;


import com.cn.xiguaapp.r2dbc.orm.rdb.executor.wrapper.ResultWrapper;
import com.cn.xiguaapp.r2dbc.orm.rdb.executor.wrapper.ResultWrappers;
import com.cn.xiguaapp.r2dbc.orm.rdb.mapping.EntityColumnMapping;
import com.cn.xiguaapp.r2dbc.orm.rdb.mapping.SyncQuery;
import com.cn.xiguaapp.r2dbc.orm.rdb.mapping.events.MappingEventTypes;
import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.TableOrViewMetadata;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.DMLOperator;

import java.util.List;
import java.util.Optional;

import static com.cn.xiguaapp.r2dbc.orm.rdb.event.ContextKeys.source;
import static com.cn.xiguaapp.r2dbc.orm.rdb.executor.wrapper.ResultWrappers.*;
import static com.cn.xiguaapp.r2dbc.orm.rdb.mapping.events.EventSupportWrapper.eventWrapper;
import static com.cn.xiguaapp.r2dbc.orm.rdb.mapping.events.MappingContextKeys.*;
import static com.cn.xiguaapp.r2dbc.orm.rdb.oprator.dml.query.Selects.count1;


/**
 * @author xiguaapp
 */
public class DefaultSyncQuery<T> extends DefaultQuery<T, SyncQuery<T>> implements SyncQuery<T> {

    public DefaultSyncQuery(TableOrViewMetadata tableMetadata, EntityColumnMapping mapping, DMLOperator operator, ResultWrapper<T, ?> wrapper) {
        super(tableMetadata, mapping, operator, wrapper);
    }

    @Override
    public List<T> fetch() {
        return operator
                .query(tableMetadata)
                .select(getSelectColumn())
                .selectExcludes(param.getExcludes())
                .where(param.getTerms())
                .orderBy(getSortOrder())
                .when(param.isPaging(), query -> query.paging(param.getPageIndex(), param.getPageSize()))
                .accept(queryOperator ->
                        tableMetadata.fireEvent(MappingEventTypes.select_before, eventContext ->
                                eventContext.set(
                                        source(DefaultSyncQuery.this),
                                        query(queryOperator),
                                        dml(operator),
                                        executorType("sync"),
                                        type("fetch")
                                )))
                .fetch(eventWrapper(tableMetadata, list(wrapper), executorType("sync"), type("fetch")))
                .sync();
    }

    @Override
    public Optional<T> fetchOne() {
        return operator
                .query(tableMetadata)
                .select(getSelectColumn())
                .where(param.getTerms())
                .orderBy(getSortOrder())
                .paging(0, 1)
                .accept(queryOperator ->
                        tableMetadata.fireEvent(
                                MappingEventTypes.select_before,
                                source(DefaultSyncQuery.this),
                                query(queryOperator),
                                dml(operator),
                                executorType("sync"),
                                type("fetchOne")
                        ))
                .fetch(eventWrapper(tableMetadata, optional(ResultWrappers.single(wrapper)), executorType("sync"), type("fetchOne")))
                .sync();
    }

    @Override
    public int count() {
        return operator
                .query(tableMetadata)
                .select(count1().as("total"))
                .where(param.getTerms())
                .accept(queryOperator ->
                        tableMetadata.fireEvent(
                                MappingEventTypes.select_before,
                                source(DefaultSyncQuery.this),
                                query(queryOperator),
                                dml(operator),
                                executorType("sync"),
                                type("count")
                        ))
                .fetch(optional(ResultWrappers.single(column("total", Number.class::cast))))
                .sync()
                .map(Number::intValue)
                .orElse(0);
    }


}
