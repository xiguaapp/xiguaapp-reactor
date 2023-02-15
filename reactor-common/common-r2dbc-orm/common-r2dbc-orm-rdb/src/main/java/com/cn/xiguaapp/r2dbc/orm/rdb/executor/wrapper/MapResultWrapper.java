package com.cn.xiguaapp.r2dbc.orm.rdb.executor.wrapper;

import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

@SuppressWarnings("all")
public class MapResultWrapper extends AbstractMapResultWrapper<Map<String, Object>> {

    @Getter
    @Setter
    private Supplier<Map<String, Object>> mapBuilder = () -> new LinkedHashMap<String, Object>();

    private static final MapResultWrapper DEFAULT_INSTANCE = new MapResultWrapper() {
        @Override
        public Map<String, Object> getResult() {
            throw new UnsupportedOperationException();
        }
    };

    public static MapResultWrapper defaultInstance() {
        return DEFAULT_INSTANCE;
    }

    @Override
    public Map<String, Object> newRowInstance() {
        return mapBuilder.get();
    }


}
