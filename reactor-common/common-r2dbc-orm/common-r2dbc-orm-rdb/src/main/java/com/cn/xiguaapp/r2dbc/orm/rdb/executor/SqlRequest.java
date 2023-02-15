package com.cn.xiguaapp.r2dbc.orm.rdb.executor;

/**
 * @author xiguaapp
 * @see SqlRequests
 */
public interface SqlRequest {

    String getSql();

    Object[] getParameters();

    boolean isEmpty();

    default boolean isNotEmpty() {
        return !isEmpty();
    }
}
