package com.cn.xiguaapp.r2dbc.orm.rdb.metadata;

import com.cn.xiguaapp.r2dbc.orm.core.DefaultValue;
import com.cn.xiguaapp.r2dbc.orm.core.DefaultValueGenerator;
import com.cn.xiguaapp.r2dbc.orm.core.RuntimeDefaultValue;
import com.cn.xiguaapp.r2dbc.orm.meta.ObjectMetadata;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.function.Supplier;

/**
 * @author xiguaapp
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LazyDefaultValueGenerator<T extends ObjectMetadata> implements DefaultValueGenerator<T > {

    private Supplier<DefaultValueGenerator<T>> generator;

    public static <T extends ObjectMetadata> LazyDefaultValueGenerator<T> of(Supplier<DefaultValueGenerator<T>> generator) {
        LazyDefaultValueGenerator<T> valueGenerator = new LazyDefaultValueGenerator<>();

        valueGenerator.generator = generator;
        return valueGenerator;
    }

    private volatile DefaultValue defaultValue;

    @Override
    public String getSortId() {
        return generator.get().getSortId();
    }

    @Override
    public RuntimeDefaultValue generate(T meta) {

        return () -> {
            if (defaultValue == null) {
                defaultValue = generator.get().generate(meta);
            }
            return defaultValue.get();
        };
    }

    @Override
    public String getName() {
        return generator.get().getName();
    }
}
