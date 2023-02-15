package com.cn.xiguapp.common.core.excel.poi.options;

import com.cn.xiguapp.common.core.excel.ExcelOption;
import org.apache.poi.ss.usermodel.Row;

/**
 * @author xiguaapp
 */
public interface RowOption extends ExcelOption {

    void row(Row row);

}
