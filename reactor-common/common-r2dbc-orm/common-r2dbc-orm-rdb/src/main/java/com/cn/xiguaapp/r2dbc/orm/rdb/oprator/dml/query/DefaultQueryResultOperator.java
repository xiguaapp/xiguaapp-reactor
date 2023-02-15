package com.cn.xiguaapp.r2dbc.orm.rdb.oprator.dml.query;

import com.cn.xiguaapp.r2dbc.orm.rdb.executor.SqlRequest;
import com.cn.xiguaapp.r2dbc.orm.rdb.executor.SyncSqlExecutor;
import com.cn.xiguaapp.r2dbc.orm.rdb.executor.reactive.ReactiveSqlExecutor;
import com.cn.xiguaapp.r2dbc.orm.rdb.executor.wrapper.ResultWrapper;
import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.RDBDatabaseMetadata;
import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.TableOrViewMetadata;
import com.cn.xiguaapp.r2dbc.orm.rdb.utils.ExceptionUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Supplier;

class DefaultQueryResultOperator<E, R> implements QueryResultOperator<E, R> {

    private Supplier<SqlRequest> sqlRequest;
    private RDBDatabaseMetadata metadata;
    private ResultWrapper<E, R> wrapper;

    public DefaultQueryResultOperator(Supplier<SqlRequest> sqlRequest,
                                      TableOrViewMetadata tableOrViewMetadata,
                                      ResultWrapper<E, R> wrapper) {
        this.sqlRequest = sqlRequest;
        this.metadata = tableOrViewMetadata.getSchema().getDatabase();
        this.wrapper = wrapper;
    }

    protected ResultWrapper<E, R> getWrapper() {

        return wrapper;
    }

    @Override
    public R sync() {
        return ExceptionUtils.translation(() -> metadata
                .findFeatureNow(SyncSqlExecutor.ID)
                .select(sqlRequest.get(), getWrapper()), metadata);
    }

    @Override
    @SuppressWarnings("all")
    public Flux<E> reactive() {
        return Flux.defer(() -> {
            return metadata.findFeatureNow(ReactiveSqlExecutor.ID)
                    .select(Mono.fromSupplier(sqlRequest), getWrapper())
                    .onErrorMap(error -> ExceptionUtils.translation(metadata, error));
        });
    }
}
