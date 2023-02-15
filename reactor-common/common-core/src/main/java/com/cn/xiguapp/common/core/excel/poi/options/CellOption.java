package com.cn.xiguapp.common.core.excel.poi.options;

import com.cn.xiguapp.common.core.excel.ExcelOption;
import com.cn.xiguapp.common.core.excel.WritableCell;
import org.apache.poi.ss.usermodel.Cell;

/**
 * @author xiguaapp
 */
public interface CellOption extends ExcelOption {

    void cell(Cell poiCell, WritableCell cell);

}
