package com.cn.xiguapp.common.core.excel.converter;

import com.cn.xiguapp.common.core.excel.Cell;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author xiguaapp
 */
public class MapWrapper extends RowWrapper<Map<String, Object>> {

    private Map<String, String> headerMapping = new HashMap<>();

    public MapWrapper header(String header, String key) {
        headerMapping.put(header, key);
        return this;
    }

    @Override
    protected Map<String, Object> newInstance() {
        return new LinkedHashMap<>();
    }

    @Override
    protected Map<String, Object> wrap(Map<String, Object> instance, Cell header, Cell dataCell) {

        String headerText = header.valueAsText().orElse("null");

        instance.put(headerMapping.getOrDefault(headerText, headerText), dataCell.value().orElse(null));

        return instance;
    }


}
