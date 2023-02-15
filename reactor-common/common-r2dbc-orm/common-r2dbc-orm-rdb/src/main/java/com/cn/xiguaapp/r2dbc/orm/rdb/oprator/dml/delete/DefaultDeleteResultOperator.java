package com.cn.xiguaapp.r2dbc.orm.rdb.oprator.dml.delete;

import com.cn.xiguaapp.r2dbc.orm.rdb.executor.SqlRequest;
import com.cn.xiguaapp.r2dbc.orm.rdb.executor.SyncSqlExecutor;
import com.cn.xiguaapp.r2dbc.orm.rdb.executor.reactive.ReactiveSqlExecutor;
import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.RDBTableMetadata;
import com.cn.xiguaapp.r2dbc.orm.rdb.utils.ExceptionUtils;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.function.Supplier;

/**
 * @author xiguaapp
 * @desc
 */
@AllArgsConstructor(staticName = "of")
public class DefaultDeleteResultOperator implements DeleteResultOperator{

    private RDBTableMetadata table;

    private Supplier<SqlRequest> sql;

    @Override
    public Integer sync() {
        return ExceptionUtils.translation(() -> table.findFeature(SyncSqlExecutor.ID)
                .map(executor -> executor.update(sql.get()))
                .orElseThrow(() -> new UnsupportedOperationException("unsupported SyncSqlExecutor")), table);
    }

    @Override
    public Mono<Integer> reactive() {
       return Mono.defer(()-> table.findFeatureNow(ReactiveSqlExecutor.ID)
               .update(Mono.fromSupplier(sql))
               .onErrorMap(error -> ExceptionUtils.translation(table, error)));
    }

}
