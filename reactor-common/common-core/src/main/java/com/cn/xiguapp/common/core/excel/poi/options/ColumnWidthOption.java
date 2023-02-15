package com.cn.xiguapp.common.core.excel.poi.options;

import com.cn.xiguapp.common.core.excel.WritableCell;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.apache.poi.ss.usermodel.Cell;

@Getter
@Setter
@AllArgsConstructor
class ColumnWidthOption implements CellOption {

    private int columnIndex;

    private int width;

    @Override
    public void cell(Cell poiCell, WritableCell cell) {
        if (cell.getRowIndex() == 0 && columnIndex == cell.getColumnIndex()) {
            poiCell.getRow()
                    .getSheet()
                    .setColumnWidth(columnIndex, width);
        }
    }


}
