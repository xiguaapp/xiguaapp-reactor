package com.cn.xiguaapp.r2dbc.orm.rdb.support.postgres;

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
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.dml.upsert.*;
import com.cn.xiguaapp.r2dbc.orm.rdb.utils.ExceptionUtils;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

@SuppressWarnings("all")
public class PostgresqlBatchUpsertOperator implements SaveOrUpdateOperator {

    private RDBTableMetadata table;

    private PostgresqlUpsertBatchInsertSqlBuilder builder;

    private RDBColumnMetadata idColumn;

    private SaveOrUpdateOperator fallback;

    public PostgresqlBatchUpsertOperator(RDBTableMetadata table) {
        this.table = table;
        this.builder = new PostgresqlUpsertBatchInsertSqlBuilder(table);
        this.idColumn = table.getColumns()
                .stream().filter(RDBColumnMetadata::isPrimaryKey)
                .findFirst().orElse(null);
        this.fallback = new DefaultSaveOrUpdateOperator(table);
    }

    @Override
    public SaveResultOperator execute(UpsertOperatorParameter parameter) {
        if (idColumn == null) {
            this.idColumn = table.getColumns()
                    .stream()
                    .filter(RDBColumnMetadata::isPrimaryKey)
                    .findFirst()
                    .orElse(null);

            if (this.idColumn == null) {
                return fallback.execute(parameter);
            }
        }

        return new PostgresqlSaveResultOperator(() -> builder.build(new PostgresqlUpsertOperatorParameter(parameter)));
    }

    class PostgresqlUpsertOperatorParameter extends InsertOperatorParameter {

        private boolean doNoThingOnConflict;

        public PostgresqlUpsertOperatorParameter(UpsertOperatorParameter parameter) {
            doNoThingOnConflict = parameter.isDoNothingOnConflict();
            setColumns(parameter.toInsertColumns());
            setValues(parameter.getValues());
        }

    }

    @AllArgsConstructor
    private class PostgresqlSaveResultOperator implements SaveResultOperator {

        Supplier<SqlRequest> sqlRequest;

        @Override
        public SaveResult sync() {
            return ExceptionUtils.translation(() -> {
                SyncSqlExecutor sqlExecutor = table.findFeatureNow(SyncSqlExecutor.ID);
                int updated = sqlExecutor.update(sqlRequest.get());
                return SaveResult.of(0, updated);
            }, table);
        }

        @Override
        public Mono<SaveResult> reactive() {
            return Mono.defer(() -> {
                return Mono.just(sqlRequest.get())
                        .as(table.findFeatureNow(ReactiveSqlExecutor.ID)::update)
                        .map(i -> SaveResult.of(0, i))
                        .as(ExceptionUtils.translation(table));
            });
        }
    }

    private class PostgresqlUpsertBatchInsertSqlBuilder extends BatchInsertSqlBuilder {

        public PostgresqlUpsertBatchInsertSqlBuilder(RDBTableMetadata table) {
            super(table);
        }

        @Override
        protected PrepareSqlFragments afterBuild(Set<InsertColumn> columns, InsertOperatorParameter parameter, PrepareSqlFragments sql) {
            sql.addSql("on conflict (", idColumn.getName(), ") do ");

            if (((PostgresqlUpsertOperatorParameter) parameter).doNoThingOnConflict) {
                sql.addSql("nothing");
                return sql;
            }
            sql.addSql("update set");

            List<Object> values = parameter.getValues().get(0);

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
                sql.addSql(columnMetadata.getFullName("excluded"));
            }

            return sql;
        }

    }
}
