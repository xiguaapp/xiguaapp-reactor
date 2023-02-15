package com.cn.xiguaapp.r2dbc.orm.rdb.oprator;

import com.cn.xiguaapp.r2dbc.orm.rdb.executor.SyncSqlExecutor;
import com.cn.xiguaapp.r2dbc.orm.rdb.executor.reactive.ReactiveSqlExecutor;
import com.cn.xiguaapp.r2dbc.orm.rdb.mapping.ReactiveRepository;
import com.cn.xiguaapp.r2dbc.orm.rdb.mapping.SyncRepository;
import com.cn.xiguaapp.r2dbc.orm.rdb.mapping.defaults.record.RecordReactiveRepository;
import com.cn.xiguaapp.r2dbc.orm.rdb.mapping.defaults.record.RecordSyncRepository;
import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.RDBDatabaseMetadata;
import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.RDBSchemaMetadata;
import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.RDBTableMetadata;
import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.TableOrViewMetadata;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.ddl.DefaultTableBuilder;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.ddl.TableBuilder;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.dml.QueryOperator;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.dml.delete.DeleteOperator;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.dml.delete.ExecutableDeleteOperator;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.dml.insert.ExecutableInsertOperator;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.dml.insert.InsertOperator;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.dml.query.ExecutableQueryOperator;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.dml.update.ExecutableUpdateOperator;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.dml.update.UpdateOperator;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.dml.upsert.DefaultUpsertOperator;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.dml.upsert.UpsertOperator;
import com.cn.xiguaapp.r2dbc.orm.rdb.mapping.defaults.record.Record;
import lombok.AllArgsConstructor;
/**
 * @author xiguaapp
 * @desc 默认数据库操作类 包括数据库操作 dml操作 sql操作 ddl操作
 */
@AllArgsConstructor(staticName = "of")
public class DefaultDatabaseOperator
        implements DatabaseOperator, DMLOperator, SQLOperator, DDLOperator {

    private final RDBDatabaseMetadata metadata;

    @Override
    public RDBDatabaseMetadata getMetadata() {
        return metadata;
    }

    @Override
    public DMLOperator dml() {
        return this;
    }

    @Override
    public DDLOperator ddl() {
        return this;
    }

    @Override
    public SQLOperator sql() {
        return this;
    }

    @Override
    public QueryOperator query(TableOrViewMetadata tableOrView) {
        return new ExecutableQueryOperator(tableOrView);
    }

    @Override
    public DeleteOperator delete(RDBTableMetadata table) {
        return ExecutableDeleteOperator.of(table);
    }

    @Override
    public UpdateOperator update(RDBTableMetadata table) {
        return ExecutableUpdateOperator.of(table);
    }

    @Override
    public UpsertOperator upsert(RDBTableMetadata table) {
        return DefaultUpsertOperator.of(table);
    }

    @Override
    public InsertOperator insert(RDBTableMetadata table) {
        return ExecutableInsertOperator.of(table);
    }

    @Override
    public DeleteOperator delete(String table) {
        return ExecutableDeleteOperator.of(metadata
                .getTable(table)
                .orElseThrow(() -> new UnsupportedOperationException("table [" + table + "] doesn't exist ")));
    }

    @Override
    public UpsertOperator upsert(String table) {
        return DefaultUpsertOperator.of(metadata
                .getTable(table)
                .orElseThrow(() -> new UnsupportedOperationException("table [" + table + "] doesn't exist ")));
    }

    @Override
    public QueryOperator query(String tableOrView) {
        return new ExecutableQueryOperator(metadata
                .getTableOrView(tableOrView)
                .orElseThrow(() -> new UnsupportedOperationException("table or view [" + tableOrView + "] doesn't exist ")));
    }

    @Override
    public UpdateOperator update(String table) {

        return ExecutableUpdateOperator.of(metadata
                .getTable(table)
                .orElseThrow(() -> new UnsupportedOperationException("table [" + table + "] doesn't exist ")));
    }

    @Override
    public InsertOperator insert(String table) {
        return ExecutableInsertOperator.of(metadata
                .getTable(table)
                .orElseThrow(() -> new UnsupportedOperationException("table [" + table + "] doesn't exist ")));
    }

    @Override
    public SyncSqlExecutor sync() {
        return metadata.getFeature(SyncSqlExecutor.ID)
                .orElseThrow(() -> new UnsupportedOperationException("unsupported SyncSqlExecutor"));
    }

    @Override
    public ReactiveSqlExecutor reactive() {
        return metadata.getFeature(ReactiveSqlExecutor.ID)
                .orElseThrow(() -> new UnsupportedOperationException("unsupported ReactiveSqlExecutor"));
    }

    @Override
    public TableBuilder createOrAlter(String name) {
        RDBTableMetadata table = metadata.getTable(name)
                .map(RDBTableMetadata::clone)
                .orElseGet(() -> {
                    String tableName = name;
                    RDBSchemaMetadata schema;
                    if (name.contains(".")) {
                        String[] arr = name.split("[.]");
                        tableName = arr[1];
                        schema = metadata.getSchema(arr[0]).orElseThrow(() -> new UnsupportedOperationException("schema [" + arr[0] + "] doesn't exist "));
                    } else {
                        schema = metadata.getCurrentSchema();
                    }

                    return schema.newTable(tableName);
                });

        return new DefaultTableBuilder(table);
    }

    @Override
    public TableBuilder createOrAlter(RDBTableMetadata newTable) {

        return new DefaultTableBuilder(newTable);
    }

    @Override
    public <K> ReactiveRepository<Record, K> createReactiveRepository(String tableName) {
        return  new RecordReactiveRepository<>(this, tableName);
    }

    @Override
    public <K> SyncRepository<Record, K> createRepository(String tableName) {
        return new RecordSyncRepository<>(this,tableName);
    }
}
