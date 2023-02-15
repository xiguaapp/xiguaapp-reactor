package com.cn.xiguaapp.r2dbc.orm.rdb.event;

import java.util.Optional;

/**
 * @author xiguaapp
 */
public interface EventContext {

    Object get(String key);

    <T> Optional<T> get(ContextKey<T> key);

    <T> EventContext set(ContextKey<T> key, T value);

    <T> EventContext set(String key, T value);

     EventContext set(ContextKeyValue<?>... keyValue);

    static EventContext create() {
        return new DefaultEventContext();
    }
}
