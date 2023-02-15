package com.cn.xiguapp.common.core.excel.converter;

import com.cn.xiguapp.common.core.excel.CellDataType;
import com.cn.xiguapp.common.core.excel.ExcelHeader;
import com.cn.xiguapp.common.core.excel.WritableCell;
import lombok.Getter;
import org.apache.commons.beanutils.BeanUtilsBean;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

/**
 * @author xiguaapp
 */
public class MapRowExpander implements BiFunction<Long, Map<String, Object>, Flux<WritableCell>> {

    @Getter
    private final List<ExcelHeader> headers = new ArrayList<>();

    public MapRowExpander header(String key, String header, CellDataType type) {
        return header(new ExcelHeader(key, header, type));
    }

    public MapRowExpander header(String key, String header) {
        return header(key, header, CellDataType.STRING);
    }

    public MapRowExpander header(ExcelHeader header) {
        headers.add(header);
        return this;
    }

    public MapRowExpander headers(Collection<ExcelHeader> headers) {
        this.headers.addAll(headers);
        return this;
    }

    @Override
    public synchronized Flux<WritableCell> apply(Long rowIndex, Map<String, Object> val) {
        return Flux
                .fromIterable(headers)
                .index()
                .map(header -> new SimpleWritableCell(
                        header.getT2(),
                        getValue(header.getT2().getKey(), val),
                        rowIndex,
                        header.getT1().intValue(),
                        header.getT1().intValue() == headers.size() - 1));
    }

    protected Object getValue(String key, Map<String, Object> map) {
        Object val = map.get(key);
        if (val != null) {
            return val;
        }
        if (key.contains(".") || key.contains("[")) {
            try {
                return BeanUtilsBean.getInstance().getPropertyUtils().getProperty(map, key);
            } catch (Exception ignore) {
            }
        }
        return null;
    }
}
