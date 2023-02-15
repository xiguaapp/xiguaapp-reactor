package com.cn.xiguaapp.r2dbc.orm.rdb.event;


import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.TableOrViewMetadata;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.DatabaseOperator;

/**
 * @author xiguaapp
 */
public interface ContextKeys {

    ContextKey<TableOrViewMetadata> table = ContextKey.of("table");

    ContextKey<?> source = ContextKey.of("source");

    ContextKey<DatabaseOperator> database = ContextKey.of("database");

    static <T> ContextKeyValue<T> source(T source) {
        return ContextKeys.<T>source().value(source);
    }

    static <T> ContextKey<T> source() {
        return (ContextKey) source;
    }

    static <T> ContextKeyValue<TableOrViewMetadata> tableMetadata(TableOrViewMetadata metadata) {
        return ContextKeyValue.of(table, metadata);
    }

}
