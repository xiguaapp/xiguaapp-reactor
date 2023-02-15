package com.cn.xiguapp.common.core.excel.poi.options;

import com.cn.xiguapp.common.core.excel.ExcelOption;
import org.apache.poi.ss.usermodel.Workbook;

/**
 * @author xiguaapp
 */
public interface WorkbookOption extends ExcelOption {

    void workbook(Workbook workBook);

}
