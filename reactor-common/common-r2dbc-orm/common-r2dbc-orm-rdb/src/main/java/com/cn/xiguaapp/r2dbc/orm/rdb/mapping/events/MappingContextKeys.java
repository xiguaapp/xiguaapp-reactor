package com.cn.xiguaapp.r2dbc.orm.rdb.mapping.events;


import com.cn.xiguaapp.r2dbc.orm.rdb.event.ContextKey;
import com.cn.xiguaapp.r2dbc.orm.rdb.event.ContextKeyValue;
import com.cn.xiguaapp.r2dbc.orm.rdb.executor.wrapper.ColumnWrapperContext;
import com.cn.xiguaapp.r2dbc.orm.rdb.mapping.EntityColumnMapping;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.DMLOperator;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.dml.QueryOperator;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.dml.delete.DeleteOperator;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.dml.insert.InsertOperator;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.dml.update.UpdateOperator;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.dml.upsert.UpsertOperator;

import java.util.Map;

@SuppressWarnings("all")
public interface MappingContextKeys {

    ContextKey<?> result = ContextKey.of("result");
    ContextKey repository = ContextKey.of("repository");

    ContextKey<?> instance = ContextKey.of("instance");

    ContextKey<Map<String,Object>> updateColumnInstance = ContextKey.of("updateColumnInstance");

    ContextKey<?> context = ContextKey.of("context");
    ContextKey<String> type = ContextKey.of("type");
    ContextKey<String> executorType = ContextKey.of("executorType");

    ContextKey<Boolean> reactive = ContextKey.of("reactive");

    ContextKey<ReactiveResultHolder> reactiveResultHolder = ContextKey.of("reactive-holder");


    ContextKey<DMLOperator> dml = ContextKey.of("dml");
    ContextKey<EntityColumnMapping> columnMapping = ContextKey.of("columnMapping");

    ContextKey<QueryOperator> query = ContextKey.of("query");
    ContextKey<UpdateOperator> update = ContextKey.of("update");
    ContextKey<DeleteOperator> delete = ContextKey.of("delete");
    ContextKey<InsertOperator> insert = ContextKey.of("insert");
    ContextKey<UpsertOperator> upsert = ContextKey.of("upsert");

    ContextKey<Throwable> error = ContextKey.of("error");


    static ContextKeyValue<String> type(String val) {
        return ContextKeyValue.of(type, val);
    }

    static ContextKeyValue<String> executorType(String val) {
        return ContextKeyValue.of(executorType, val);
    }

    static <T> ContextKeyValue<DMLOperator> dml(DMLOperator operator) {
        return ContextKeyValue.of(dml, operator);
    }

    static <T> ContextKeyValue<QueryOperator> query(QueryOperator operator) {
        return ContextKeyValue.of(query, operator);
    }

    static <T> ContextKeyValue<DeleteOperator> delete(DeleteOperator operator) {
        return ContextKeyValue.of(delete, operator);
    }

    static <T> ContextKeyValue<UpdateOperator> update(UpdateOperator operator) {
        return ContextKeyValue.of(update, operator);
    }

    static <T> ContextKeyValue<InsertOperator> insert(InsertOperator operator) {
        return ContextKeyValue.of(insert, operator);
    }

    static <T> ContextKeyValue<UpsertOperator> upsert(UpsertOperator operator) {
        return ContextKeyValue.of(upsert, operator);
    }
    static <T> ContextKeyValue<Throwable> error(Throwable throwable) {
        return ContextKeyValue.of(error, throwable);
    }

    static <T> ContextKeyValue<Boolean> reactive(Boolean isReactive) {
        return ContextKeyValue.of(reactive, isReactive);
    }

    static <T> ContextKeyValue<ReactiveResultHolder> reactiveResult(ReactiveResultHolder holder) {
        return ContextKeyValue.of(reactiveResultHolder, holder);
    }

    static <T> ContextKeyValue<ColumnWrapperContext<T>> columnWrapperContext(ColumnWrapperContext<T> val) {
        return ContextKeyValue.of(context, val);
    }

    static <T> ContextKeyValue<T> instance(T val) {
        return ContextKeyValue.of(instance, val);
    }

    static <T> ContextKeyValue<EntityColumnMapping> columnMapping(EntityColumnMapping val) {
        return ContextKeyValue.of(columnMapping, val);
    }

    static <T> ContextKeyValue<Map<String,Object>> updateColumnInstance(Map<String,Object> val) {

        return ContextKeyValue.of(updateColumnInstance, val);
    }

    static <T> ContextKeyValue<T> result(T val) {
        return ContextKeyValue.of(result, val);
    }


}
