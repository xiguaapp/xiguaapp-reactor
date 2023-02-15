package com.cn.xiguaapp.r2dbc.orm.rdb.mapping;


import com.cn.xiguaapp.r2dbc.orm.param.MethodReferenceColumn;
import com.cn.xiguaapp.r2dbc.orm.param.QueryParam;
import com.cn.xiguaapp.r2dbc.orm.param.StaticMethodReferenceColumn;
import com.cn.xiguaapp.r2dbc.orm.properties.Conditional;

import java.util.Arrays;

@SuppressWarnings("all")
public interface DSLUpdate<E, ME extends DSLUpdate> extends Conditional<ME> {

    ME includes(String... properties);

    ME excludes(String... properties);

    ME set(E entity);

    ME set(String column, Object value);

    ME setNull(String column);

    default <R> ME set(MethodReferenceColumn<R> columnAndValue) {
        return set(columnAndValue.getColumn(), columnAndValue.get());
    }

    default ME set(StaticMethodReferenceColumn<E> column, Object value) {
        return set(column.getColumn(), value);
    }

    default ME setNull(StaticMethodReferenceColumn<E> column) {
        return setNull(column.getColumn());
    }

    default ME setNull(MethodReferenceColumn<E> column) {
        return setNull(column.getColumn());
    }

    default ME includes(StaticMethodReferenceColumn<E>... columns) {
        return includes(Arrays
                .stream(columns)
                .map(StaticMethodReferenceColumn::getColumn)
                .toArray(String[]::new));
    }

    default ME excludes(StaticMethodReferenceColumn<E>... columns) {
        return excludes(Arrays
                .stream(columns)
                .map(StaticMethodReferenceColumn::getColumn)
                .toArray(String[]::new));
    }

    default ME includes(MethodReferenceColumn<E>... columns) {
        return includes(Arrays
                .stream(columns)
                .map(MethodReferenceColumn::getColumn
                ).toArray(String[]::new));
    }

    default ME excludes(MethodReferenceColumn<E>... columns) {
        return excludes(Arrays
                .stream(columns)
                .map(MethodReferenceColumn::getColumn)
                .toArray(String[]::new));
    }

    public QueryParam toQueryParam();

}
