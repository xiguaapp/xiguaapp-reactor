package com.cn.xiguaapp.r2dbc.orm.rdb.oprator;

import com.cn.xiguaapp.r2dbc.orm.rdb.mapping.ReactiveRepository;
import com.cn.xiguaapp.r2dbc.orm.rdb.mapping.SyncRepository;
import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.RDBTableMetadata;
import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.TableOrViewMetadata;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.dml.QueryOperator;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.dml.delete.DeleteOperator;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.dml.insert.InsertOperator;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.dml.update.UpdateOperator;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.dml.upsert.UpsertOperator;

import com.cn.xiguaapp.r2dbc.orm.rdb.mapping.defaults.record.Record;
/**
 * @author xiguaapp
 */
public interface DMLOperator {


    QueryOperator query(TableOrViewMetadata tableOrView);

    DeleteOperator delete(RDBTableMetadata table);

    UpdateOperator update(RDBTableMetadata table);

    InsertOperator insert(RDBTableMetadata table);

    UpsertOperator upsert(RDBTableMetadata table);

    QueryOperator query(String tableOrView);

    UpdateOperator update(String table);

    InsertOperator insert(String table);

    DeleteOperator delete(String table);

    UpsertOperator upsert(String table);

    <K> SyncRepository<Record, K> createRepository(String tableName);

    <K> ReactiveRepository<Record, K> createReactiveRepository(String tableName);

}
