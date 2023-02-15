package com.cn.xiguaapp.r2dbc.orm.rdb.event;

/**
 * @author xiguaapp
 */
public interface ContextKey<T> {
    String getKey();

    static <T> ContextKey<T> of(String key) {
        return () -> key;
    }

    default ContextKeyValue<T> value(T value) {
        return new ContextKeyValue<T>() {
            @Override
            public String getKey() {
                return ContextKey.this.getKey();
            }

            @Override
            public T getValue() {
                return value;
            }
        };
    }
}
