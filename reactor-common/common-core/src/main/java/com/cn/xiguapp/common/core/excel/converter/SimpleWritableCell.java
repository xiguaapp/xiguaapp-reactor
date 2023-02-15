package com.cn.xiguapp.common.core.excel.converter;

import com.cn.xiguapp.common.core.excel.CellDataType;
import com.cn.xiguapp.common.core.excel.ExcelHeader;
import com.cn.xiguapp.common.core.excel.WritableCell;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Optional;

/**
 * @author xiguaapp
 */
@AllArgsConstructor
public class SimpleWritableCell implements WritableCell {

    @Getter
    private ExcelHeader header;

    public Object value;

    @Getter
    private long rowIndex;

    @Getter
    private int columnIndex;

    @Getter
    private boolean end;

    @Override
    public int getSheetIndex() {
        // TODO: 2020/3/17
        return 0;
    }

    @Override
    public Optional<Object> value() {
        return Optional.ofNullable(value);
    }

    @Override
    public CellDataType getType() {
        return header.getType();
    }
}
