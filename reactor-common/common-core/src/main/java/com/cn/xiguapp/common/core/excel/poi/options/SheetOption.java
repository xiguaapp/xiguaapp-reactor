package com.cn.xiguapp.common.core.excel.poi.options;

import com.cn.xiguapp.common.core.excel.ExcelOption;
import org.apache.poi.ss.usermodel.Sheet;

/**
 * @author xiguaapp
 */
public interface SheetOption extends ExcelOption {

    void sheet(Sheet sheet);

}
