package com.cn.xiguaapp.common.r2dbc.starter.deprecated.convert;

import cn.hutool.core.util.ArrayUtil;
import com.cn.xiguapp.common.core.utils.SnowflakeIdGenerator;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.r2dbc.mapping.OutboundRow;
import org.springframework.data.r2dbc.mapping.SettableValue;
import org.springframework.data.relational.core.mapping.Column;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * @author xiguaapp
 * @desc 新版本后移除
 */
@Deprecated
public abstract class BaseConvert<T> implements Converter<T, OutboundRow> {
    @Override
    public OutboundRow convert(T source) {
        OutboundRow row = new OutboundRow();
        Class<?> clz = source.getClass();
        Stream.of(ArrayUtil.addAll(clz.getDeclaredFields(),clz.getSuperclass().getDeclaredFields())).forEach(x -> {
            x.setAccessible(true);
            String name = x.getName();
            Object val = null;
            try {
                val = x.get(source);
            } catch (IllegalAccessException ignored) {
            }
            if ("id".equals(name))
                row.put("id", SettableValue.from(Optional.ofNullable(val)
                        .orElse(SnowflakeIdGenerator.getInstance())));
            else {
                if (Objects.nonNull(val)) {
                    Column column = x.getAnnotation(Column.class);
                    row.put(Objects.isNull(column) ? name : column.value(), SettableValue.fromOrEmpty(val, x.getType()));
                }
            }
        });
        return row;
    }
}