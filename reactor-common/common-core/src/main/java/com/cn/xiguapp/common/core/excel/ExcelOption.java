package com.cn.xiguapp.common.core.excel;

/**
 * @author xiguaapp
 */
public interface ExcelOption {

    default Class<?> getType() {
        return getClass().getInterfaces()[0];
    }

    default <R> R unwrap(Class<R> type) {
        return type.cast(this);
    }
}
