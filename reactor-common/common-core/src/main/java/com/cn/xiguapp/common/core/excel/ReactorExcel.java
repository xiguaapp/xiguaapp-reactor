package com.cn.xiguapp.common.core.excel;

import com.cn.xiguapp.common.core.excel.converter.RowWrapper;
import com.cn.xiguapp.common.core.excel.spi.ExcelReader;
import com.cn.xiguapp.common.core.excel.spi.ExcelWriter;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

import java.io.InputStream;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author xiguaapp
 * @desc Reactor Excel,CSV Lib
 */
@Slf4j
public abstract class ReactorExcel {

    private static Map<String, ExcelReader> readers = new ConcurrentHashMap<>();
    private static Map<String, ExcelWriter> writers = new ConcurrentHashMap<>();

    static {
        try {
            ServiceLoader.load(ExcelReader.class)
                    .forEach(reader -> {
                        for (String excelFormat : reader.getSupportFormat()) {
                            readers.put(excelFormat, reader);
                        }
                    });

            ServiceLoader.load(ExcelWriter.class)
                    .forEach(reader -> {
                        for (String excelFormat : reader.getSupportFormat()) {
                            writers.put(excelFormat, reader);
                        }
                    });
        } catch (Exception e) {
            log.error("load excel reader error", e);
        }
    }

    public static ExcelReader lookupReader(String format) {
        ExcelReader reader = readers.get(format);
        if (reader == null) {
            throw new UnsupportedOperationException("unsupported format:" + format);
        }
        return reader;
    }

    public static ExcelWriter lookupWriter(String format) {
        ExcelWriter writer = writers.get(format);
        if (writer == null) {
            throw new UnsupportedOperationException("unsupported format:" + format);
        }
        return writer;
    }

    public static <T> Flux<T> read(InputStream input,
                                   String format,
                                   RowWrapper<T> wrapper) {
        return lookupReader(format)
                .read(input)
                .flatMap(wrapper);
    }

    public static ReaderOperator<Map<String, Object>> mapReader(String format) {
        return ReaderOperator.ofMap(lookupReader(format));
    }

    public static <T> ReaderOperator<T> reader(Class<T> type, String format) {
        return ReaderOperator.of(lookupReader(format), type);
    }

    public static <T> WriterOperator<T> writer(String format) {
        return WriterOperator.of(lookupWriter(format));
    }


}
