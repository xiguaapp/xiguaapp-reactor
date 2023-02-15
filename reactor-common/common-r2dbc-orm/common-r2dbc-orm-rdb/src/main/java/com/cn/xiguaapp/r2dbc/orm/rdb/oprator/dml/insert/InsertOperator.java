package com.cn.xiguaapp.r2dbc.orm.rdb.oprator.dml.insert;


import com.cn.xiguaapp.r2dbc.orm.param.MethodReferenceColumn;
import com.cn.xiguaapp.r2dbc.orm.param.StaticMethodReferenceColumn;
import com.cn.xiguaapp.r2dbc.orm.rdb.executor.SqlRequest;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public abstract class InsertOperator {

    public abstract InsertOperator columns(String... columns);

    public abstract InsertOperator values(Object... values);

    public abstract InsertOperator value(String column, Object value);

    @SafeVarargs
    public final <T> InsertOperator columns(StaticMethodReferenceColumn<T>... columns) {
        return columns(Arrays.stream(columns)
                .map(StaticMethodReferenceColumn::getColumn)
                .toArray(String[]::new));
    }

    @SafeVarargs
    public final <T> InsertOperator values(MethodReferenceColumn<T>... columns) {
        String[] column = new String[columns.length];
        Object[] value = new Object[columns.length];

        for (int i = 0; i < columns.length; i++) {
            column[i] = columns[i].getColumn();
            value[i] = columns[i].get();
        }

        return columns(column).values(value);
    }

    public InsertOperator value(Map<String, Object> values) {
        values.forEach(this::value);
        return this;
    }

    public abstract InsertOperator values(List<Map<String, Object>> values);

    public abstract SqlRequest getSql();

    public abstract InsertResultOperator execute();

}
