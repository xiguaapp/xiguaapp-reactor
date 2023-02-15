package com.cn.xiguapp.common.core.excel;

import com.cn.xiguapp.common.core.excel.converter.MapWrapper;
import com.cn.xiguapp.common.core.excel.spi.ExcelReader;
import lombok.SneakyThrows;
import org.apache.commons.beanutils.BeanUtils;
import reactor.core.publisher.Flux;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * @author xiguaapp
 */
public class ReaderOperator<T> {

    private ExcelReader reader;

    private Class<T> targetType;

    private MapWrapper wrapper = new MapWrapper();

    private Function<Map<String, Object>, T> converter = this::copy;

    static <T> ReaderOperator<T> of(ExcelReader reader, Class<T> targetType) {
        return new ReaderOperator<>(reader, targetType);
    }

    @SuppressWarnings("all")
    static ReaderOperator<Map<String, Object>> ofMap(ExcelReader reader) {
        return (ReaderOperator) of(reader, Map.class);
    }

    public ReaderOperator(ExcelReader reader, Class<T> targetType) {
        this.targetType = targetType;
        this.reader = reader;
        if (targetType.isAssignableFrom(Map.class)) {
            converter = v -> (T) v;
        } else {
            headers(targetType);
        }
    }

    private List<ExcelOption> options = new ArrayList<>();

    public ReaderOperator<T> headerRowIs(int headerRowNumber) {
        wrapper.setHeaderIndex(headerRowNumber);
        return this;
    }

    public ReaderOperator<T> header(String header, String key) {
        wrapper.header(header, key);
        return this;
    }


    public ReaderOperator<T> headers(Class<T> type) {
        // TODO: 2020/3/17
        return this;
    }

    public ReaderOperator<T> converter(Function<Map<String, Object>, T> converter) {
        this.converter = converter;
        return this;
    }

    @SneakyThrows
    private T copy(Map<String, Object> dest) {
        T t = targetType.getDeclaredConstructor().newInstance();
        BeanUtils.copyProperties(t, dest);
        return t;
    }

    public ReaderOperator<T> options(ExcelOption... options) {
        this.options.addAll(Arrays.asList(options));
        return this;
    }

    public ReaderOperator<T> option(ExcelOption option) {
        options.add(option);
        return this;
    }

    public Flux<T> read(InputStream inputStream) {
        return reader
                .read(inputStream, options.toArray(new ExcelOption[0]))
                .flatMap(wrapper)
                .map(converter);
    }

}
