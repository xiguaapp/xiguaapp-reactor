package com.cn.xiguapp.common.core.excel.csv;

import com.cn.xiguapp.common.core.excel.Cell;
import com.cn.xiguapp.common.core.excel.CellDataType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Optional;

/**
 * @author xiguaapp
 */
@Getter
@Setter
@AllArgsConstructor
public class CsvCell implements Cell {

    private long rowIndex;

    private int columnIndex;

    private String value;

    private boolean end;
    @Override
    public int getSheetIndex() {
        return 0;
    }

    @Override
    public Optional<Object> value() {
        return Optional.ofNullable(value);
    }

    @Override
    public CellDataType getType() {
        return CellDataType.STRING;
    }

}
