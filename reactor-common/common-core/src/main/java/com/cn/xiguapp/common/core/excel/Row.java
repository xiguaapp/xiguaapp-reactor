package com.cn.xiguapp.common.core.excel;

import java.util.List;

/**
 * @author xiguaapp
 */
public interface Row {

    int getRowIndex();

    Cell getCell(int column);

    List<Cell> columns();

    boolean isEnd();
}
