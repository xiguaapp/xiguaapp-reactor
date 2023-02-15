package com.cn.xiguaapp.common.r2dbc.starter.deprecated.base;

import cn.hutool.core.util.ArrayUtil;
import com.cn.xiguapp.common.core.utils.SnowflakeIdGenerator;
import com.cn.xiguaapp.common.r2dbc.starter.deprecated.r2dbc.entity.SuperEntity;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.r2dbc.mapping.OutboundRow;
import org.springframework.data.r2dbc.mapping.SettableValue;
import org.springframework.data.relational.core.mapping.Column;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

/**
 * @author xiguaapp
 * @desc 自定义id
 */
@Deprecated
public class BaseConvert<T extends SuperEntity> implements Converter<T, OutboundRow> {


    @Override
    public OutboundRow convert(T source) {
        OutboundRow row = new OutboundRow();
        Class<?> clz = source.getClass();
        AtomicReference<Object> id = new AtomicReference<>();
        Stream.of(ArrayUtil.addAll(clz.getDeclaredFields(),clz.getSuperclass().getDeclaredFields())).forEach(x -> {
            x.setAccessible(true);
            String name = x.getName();
            Object val = null;
            try {
                val = x.get(source);
            } catch (IllegalAccessException ignored) {
            }
            if ("id".equals(name)){
                id.set(Optional.ofNullable(val).orElse(null));
                row.put("id", SettableValue.from(Optional.ofNullable(val)
                        .orElse(SnowflakeIdGenerator.getInstance())));
            }
            else if ("create_time".equals(name)){
                if (Objects.isNull(id.get())){
                    row.put("create_time",SettableValue.from(Optional.ofNullable(val)
                            .orElse(LocalDateTime.now())));
                    row.put("update_time",SettableValue.from(Optional.ofNullable(val)
                            .orElse(LocalDateTime.now())));
                }
            }else if ("update_time".equals(name)){
                    if (Objects.nonNull(id.get())){
                        row.put("update_time",SettableValue.from(Optional.ofNullable(val)
                                .orElse(LocalDateTime.now())));
                    }
            } else{
                if (Objects.nonNull(val)) {
                    Column column = x.getAnnotation(Column.class);
                    row.put(Objects.isNull(column) ? name : column.value(), SettableValue.fromOrEmpty(val, x.getType()));
                }
            }
        });
        return row;
    }
}