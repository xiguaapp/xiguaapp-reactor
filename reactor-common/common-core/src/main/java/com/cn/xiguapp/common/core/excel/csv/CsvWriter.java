package com.cn.xiguapp.common.core.excel.csv;

import com.cn.xiguapp.common.core.excel.ExcelOption;
import com.cn.xiguapp.common.core.excel.WritableCell;
import com.cn.xiguapp.common.core.excel.spi.ExcelWriter;
import lombok.SneakyThrows;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

/**
 * @author xiguaapp
 */
public class CsvWriter implements ExcelWriter {

    @Override
    public String[] getSupportFormat() {
        return new String[]{"csv"};
    }

    @SneakyThrows
    private void doWrite(CSVPrinter printer, WritableCell cell) {

        printer.print(cell.valueAsText().orElse(""));
        if (cell.isEnd()) {
            printer.println();
        }
    }

    @SneakyThrows
    private void closePrinter(CSVPrinter printer) {
        printer.close();
    }

    @Override
    public Mono<Void> write(Flux<WritableCell> dataStream,
                            OutputStream outputStream,
                            ExcelOption... options) {


        return Mono.defer(() -> {
            try {
                CSVPrinter printer = new CSVPrinter(new OutputStreamWriter(outputStream), CSVFormat.EXCEL);
                return dataStream
                        .doOnNext(cell -> doWrite(printer, cell))
                        .doFinally(s -> closePrinter(printer)).then();
            } catch (IOException e) {
                return Mono.error(e);
            }

        });
    }
}
