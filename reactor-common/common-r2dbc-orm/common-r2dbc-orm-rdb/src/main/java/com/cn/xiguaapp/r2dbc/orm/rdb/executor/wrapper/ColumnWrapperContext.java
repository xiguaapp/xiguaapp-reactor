package com.cn.xiguaapp.r2dbc.orm.rdb.executor.wrapper;

/**
 * @author xiguaapp
 */
public interface ColumnWrapperContext<T> {

    int getColumnIndex();

    String getColumnLabel();

    Object getResult();

    T getRowInstance();

    void setRowInstance(T instance);
}
