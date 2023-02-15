package com.cn.xiguaapp.r2dbc.orm.rdb.mapping;


public interface SyncUpdate<E> extends DSLUpdate<E, SyncUpdate<E>> {

    int execute();
}
