package com.cn.xiguaapp.r2dbc.orm.rdb.executor;

import com.cn.xiguaapp.r2dbc.orm.rdb.executor.wrapper.ColumnWrapperContext;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author xiguaapp
 */
@Getter
@Setter
@AllArgsConstructor
public class DefaultColumnWrapperContext<T> implements ColumnWrapperContext<T> {

    private int columnIndex;

    private String columnLabel;

    private Object result;

    private T rowInstance;

}
