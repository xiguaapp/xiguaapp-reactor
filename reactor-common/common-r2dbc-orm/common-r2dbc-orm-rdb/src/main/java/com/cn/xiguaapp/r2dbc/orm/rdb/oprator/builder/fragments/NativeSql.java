package com.cn.xiguaapp.r2dbc.orm.rdb.oprator.builder.fragments;

public interface NativeSql {

    Object[] EMPTY_PARAMETER = new Object[0];

    String getSql();

    default Object[] getParameters() {
        return EMPTY_PARAMETER;
    }

    static NativeSql of(String sql, Object... parameters) {
        return new NativeSql() {
            @Override
            public String getSql() {
                return sql;
            }

            @Override
            public Object[] getParameters() {
                return parameters;
            }
        };
    }
}
