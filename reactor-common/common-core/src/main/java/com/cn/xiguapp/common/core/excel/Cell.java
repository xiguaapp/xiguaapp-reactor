package com.cn.xiguapp.common.core.excel;

import java.util.Optional;

/**
 * @author xiguaapp
 */
public interface Cell {

    int getSheetIndex();

    long getRowIndex();

    int getColumnIndex();

    Optional<Object> value();

    default Optional<String> valueAsText() {
        return value()
                .map(String::valueOf);
    }

    CellDataType getType();

    boolean isEnd();
}
