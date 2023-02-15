package com.cn.xiguaapp.r2dbc.orm.rdb.executor.reactive;

import com.cn.xiguaapp.r2dbc.orm.rdb.executor.SqlRequest;
import com.cn.xiguaapp.r2dbc.orm.rdb.executor.SyncSqlExecutor;
import com.cn.xiguaapp.r2dbc.orm.rdb.executor.wrapper.ResultWrapper;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import reactor.core.publisher.Mono;

import java.util.concurrent.TimeUnit;

/**
 * @author xiguaapp
 */
@AllArgsConstructor(staticName = "of")
public class ReactiveSyncSqlExecutor implements SyncSqlExecutor {

    private final ReactiveSqlExecutor sqlExecutor;

    @Override
    @SneakyThrows
    public int update(SqlRequest request) {
        return sqlExecutor
                .update(Mono.just(request))
                .toFuture()
                .get(30, TimeUnit.SECONDS);
    }

    @Override
    @SneakyThrows
    public void execute(SqlRequest request) {
        sqlExecutor
                .execute(Mono.just(request))
                .toFuture()
                .get(30, TimeUnit.SECONDS);

    }

    @Override
    @SneakyThrows
    public <T, R> R select(SqlRequest request, ResultWrapper<T, R> wrapper) {
        sqlExecutor.select(Mono.just(request), wrapper)
                .collectList()
                .toFuture()
                .get(30, TimeUnit.SECONDS);

        return wrapper.getResult();
    }
}
