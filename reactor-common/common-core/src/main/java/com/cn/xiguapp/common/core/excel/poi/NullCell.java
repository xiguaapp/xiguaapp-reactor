package com.cn.xiguapp.common.core.excel.poi;

import com.cn.xiguapp.common.core.excel.Cell;
import com.cn.xiguapp.common.core.excel.CellDataType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Optional;

/**
 * @author xiguaapp
 */
@AllArgsConstructor
public class NullCell implements Cell {

    @Getter
    private int sheetIndex;

    @Getter
    private long rowIndex;

    @Getter
    private int columnIndex;

    @Getter
    private boolean end;



    @Override
    public Optional<Object> value() {
        return Optional.empty();
    }

    @Override
    public CellDataType getType() {
        return CellDataType.AUTO;
    }

}
