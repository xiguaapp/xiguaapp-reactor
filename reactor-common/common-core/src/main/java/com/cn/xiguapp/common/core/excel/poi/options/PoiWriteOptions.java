package com.cn.xiguapp.common.core.excel.poi.options;


import com.cn.xiguapp.common.core.excel.ExcelOption;

/**
 * @author xiguaapp
 */
public interface PoiWriteOptions {

    static ExcelOption width(int columnIndex, int width) {
        return new ColumnWidthOption(columnIndex, width);
    }

}
