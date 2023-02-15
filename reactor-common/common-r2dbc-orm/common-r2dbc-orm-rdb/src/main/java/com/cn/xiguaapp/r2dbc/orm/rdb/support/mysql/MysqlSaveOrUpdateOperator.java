package com.cn.xiguaapp.r2dbc.orm.rdb.support.mysql;

import com.cn.xiguaapp.r2dbc.orm.rdb.executor.SqlRequest;
import com.cn.xiguaapp.r2dbc.orm.rdb.executor.SyncSqlExecutor;
import com.cn.xiguaapp.r2dbc.orm.rdb.executor.reactive.ReactiveSqlExecutor;
import com.cn.xiguaapp.r2dbc.orm.rdb.mapping.defaults.SaveResult;
import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.RDBColumnMetadata;
import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.RDBTableMetadata;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.builder.fragments.NativeSql;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.builder.fragments.PrepareSqlFragments;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.builder.fragments.insert.BatchInsertSqlBuilder;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.dml.insert.InsertColumn;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.dml.insert.InsertOperatorParameter;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.dml.upsert.SaveOrUpdateOperator;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.dml.upsert.SaveResultOperator;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.dml.upsert.UpsertColumn;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.dml.upsert.UpsertOperatorParameter;
import com.cn.xiguaapp.r2dbc.orm.rdb.utils.ExceptionUtils;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * @author xiguaapp
 */
@SuppressWarnings("all")
public class MysqlSaveOrUpdateOperator implements SaveOrUpdateOperator {

    private RDBTableMetadata table;

    public MysqlUpsertBatchInsertSqlBuilder builder;

    public MysqlSaveOrUpdateOperator(RDBTableMetadata table) {
        this.table = table;
        this.builder = new MysqlUpsertBatchInsertSqlBuilder(table);
    }

    @Override
    public SaveResultOperator execute(UpsertOperatorParameter parameter) {

        return new MysqlSaveResultOperator(() -> parameter.getValues()
                .stream()
                .map(value -> {
                    InsertOperatorParameter newParam = new InsertOperatorParameter();
                    newParam.setColumns(parameter.toInsertColumns());
                    newParam.getValues().add(value);
                    return newParam;
                })
                .map(builder::build)
                .collect(Collectors.toList()));
    }

    @AllArgsConstructor
    private class MysqlSaveResultOperator implements SaveResultOperator {

        Supplier<List<SqlRequest>> sqlRequest;

        @Override
        public SaveResult sync() {
            return ExceptionUtils.translation(() -> {
                SyncSqlExecutor sqlExecutor = table.findFeatureNow(SyncSqlExecutor.ID);
                int added = 0, updated = 0;
                for (SqlRequest request : sqlRequest.get()) {
                    int num = sqlExecutor.update(request);
                    if (num == 0) {
                        updated++;
                    } else {
                        added++;
                    }
                }
                return SaveResult.of(added, updated);
            }, table);
        }

        @Override
        public Mono<SaveResult> reactive() {
            return Mono.defer(() -> {
                ReactiveSqlExecutor sqlExecutor = table.findFeatureNow(ReactiveSqlExecutor.ID);
                return Flux.fromIterable(sqlRequest.get())
                        .flatMap(sql -> sqlExecutor.update(Mono.just(sql)))
                        .map(i -> SaveResult.of(i > 0 ? 1 : 0, i == 0 ? 1 : 0))
                        .reduce(SaveResult::merge)
                        .onErrorMap(err -> ExceptionUtils.translation(table, err));
            });
        }
    }

    private class MysqlUpsertBatchInsertSqlBuilder extends BatchInsertSqlBuilder {

        public MysqlUpsertBatchInsertSqlBuilder(RDBTableMetadata table) {
            super(table);
        }

        @Override
        protected void afterValues(Set<InsertColumn> columns, List<Object> values, PrepareSqlFragments sql) {
            sql.addSql("on duplicate key update");

            int index = 0;
            boolean more = false;
            for (InsertColumn column : columns) {
                Object value = index >= values.size() ? null : values.get(index);
                index++;
                if (column instanceof UpsertColumn && ((UpsertColumn) column).isUpdateIgnore()) {
                    continue;
                }
                RDBColumnMetadata columnMetadata = table.getColumn(column.getColumn()).orElse(null);
                if (value == null
                        || columnMetadata == null
                        || columnMetadata.isPrimaryKey()
                        || !columnMetadata.isUpdatable()) {

                    continue;
                }

                if (more) {
                    sql.addSql(",");
                }
                more = true;
                sql.addSql(columnMetadata.getQuoteName()).addSql("=");
                if (value instanceof NativeSql) {
                    sql.addSql(((NativeSql) value).getSql()).addParameter(((NativeSql) value).getParameters());
                    continue;
                }

                sql.addSql("?").addParameter(columnMetadata.encode(value));
            }
        }
    }
}
