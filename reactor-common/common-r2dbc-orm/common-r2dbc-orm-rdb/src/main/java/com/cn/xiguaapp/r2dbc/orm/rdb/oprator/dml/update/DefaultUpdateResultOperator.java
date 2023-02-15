package com.cn.xiguaapp.r2dbc.orm.rdb.oprator.dml.update;

import com.cn.xiguaapp.r2dbc.orm.rdb.executor.SqlRequest;
import com.cn.xiguaapp.r2dbc.orm.rdb.executor.SyncSqlExecutor;
import com.cn.xiguaapp.r2dbc.orm.rdb.executor.reactive.ReactiveSqlExecutor;
import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.RDBTableMetadata;
import com.cn.xiguaapp.r2dbc.orm.rdb.utils.ExceptionUtils;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.function.Supplier;

@AllArgsConstructor(staticName = "of")
class DefaultUpdateResultOperator implements UpdateResultOperator {

    private RDBTableMetadata table;

    private Supplier<SqlRequest> sql;

    @Override
    public Integer sync() {
        return ExceptionUtils.translation(() -> table.findFeatureNow(SyncSqlExecutor.ID).update(sql.get()), table);
    }

    @Override
    public Mono<Integer> reactive() {
        return Mono.defer(() -> table
                .findFeatureNow(ReactiveSqlExecutor.ID)
                .update(Mono.fromSupplier(sql))
                .onErrorMap(err -> ExceptionUtils.translation(table, err))
        );
    }

}
