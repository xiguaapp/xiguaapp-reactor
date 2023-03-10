package com.cn.xiguapp.common.core.excel.csv;

import com.cn.xiguapp.common.core.excel.ExcelOption;
import com.cn.xiguapp.common.core.excel.spi.ExcelReader;
import lombok.SneakyThrows;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import reactor.core.publisher.Flux;

import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author xiguaapp
 */
public class CsvReader implements ExcelReader {

    @Override
    public String[] getSupportFormat() {
        return new String[]{"csv"};
    }

    @Override
    @SneakyThrows
    public Flux<CsvCell> read(InputStream inputStream, ExcelOption... options) {

        return Flux.create(sink -> {

            try (CSVParser parser = CSVFormat.EXCEL.parse(new InputStreamReader(inputStream))) {
                int rowIndex = 0;
                for (CSVRecord record : parser) {
                    if (sink.isCancelled()) {
                        break;
                    }
                    int last = record.size() - 1;
                    for (int i = 0; i < last; i++) {
                        sink.next(new CsvCell(rowIndex, i, record.get(i), false));
                    }
                    sink.next(new CsvCell(rowIndex, last, record.get(last), true));
                    rowIndex++;
                }
                sink.complete();
            } catch (Throwable err) {
                sink.error(err);
            }
        });
    }
}
