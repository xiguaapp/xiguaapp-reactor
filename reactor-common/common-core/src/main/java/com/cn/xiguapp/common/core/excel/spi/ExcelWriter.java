package com.cn.xiguapp.common.core.excel.spi;

import com.cn.xiguapp.common.core.excel.ExcelOption;
import com.cn.xiguapp.common.core.excel.WritableCell;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.OutputStream;

/**
 * @author xiguaapp
 */
public interface ExcelWriter {

    String[] getSupportFormat();

    Mono<Void> write(Flux<WritableCell> dataStream,
                     OutputStream outputStream,
                     ExcelOption... options);

}
