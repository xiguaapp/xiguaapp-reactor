package com.cn.xiguaapp.r2dbc.orm.rdb.metadata;

import com.cn.xiguaapp.r2dbc.orm.core.DefaultValue;
import com.cn.xiguaapp.r2dbc.orm.core.DefaultValueGenerator;
import com.cn.xiguaapp.r2dbc.orm.core.RuntimeDefaultValue;
import com.cn.xiguaapp.r2dbc.orm.meta.ObjectMetadata;
import lombok.Getter;

public class FixedDefaultValueGenerator<M extends ObjectMetadata> implements DefaultValueGenerator<M> {

    private DefaultValue value;

    @Getter
    private String sortId;

    @Getter
    private String name;

    public static <M extends ObjectMetadata> FixedDefaultValueGenerator<M> of(Object value) {
        FixedDefaultValueGenerator<M> generator = new FixedDefaultValueGenerator<>();
        generator.sortId = "fixed:" + value;
        generator.name = "固定值:" + value;
        generator.value = (RuntimeDefaultValue) () -> value;
        return generator;
    }

    @Override
    public DefaultValue generate(M metadata) {
        return value;
    }

}
