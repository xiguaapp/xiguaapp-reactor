package com.cn.xiguapp.common.core.excel.spi;

import com.cn.xiguapp.common.core.excel.Cell;
import com.cn.xiguapp.common.core.excel.ExcelOption;
import reactor.core.publisher.Flux;

import java.io.InputStream;

/**
 * @author xiguaapp
 */
public interface ExcelReader {

    String[] getSupportFormat();

    Flux<? extends Cell> read(InputStream inputStream,
                              ExcelOption... options);

}
