package com.cn.xiguaapp.r2dbc.common.core.generator;

import com.cn.xiguaapp.r2dbc.orm.core.DefaultValue;
import com.cn.xiguaapp.r2dbc.orm.core.DefaultValueGenerator;
import com.cn.xiguaapp.r2dbc.orm.core.RuntimeDefaultValue;
import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.RDBColumnMetadata;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author xiguaapp
 * @desc 时间戳生成随机码
 * @see DefaultValueGenerator
 * @since 1.0 22:08
 */
public class CurrentTimeGenerator implements DefaultValueGenerator<RDBColumnMetadata> {
    @Override
    public String getSortId() {
        return Generators.CURRENT_TIME;
    }

    @Override
    public DefaultValue generate(RDBColumnMetadata metadata) {
        return (RuntimeDefaultValue) () -> generic(metadata.getJavaType());
    }

    protected Object generic(Class type) {
        if (type == Date.class) {
            return new Date();
        }
        if (type == java.sql.Date.class) {
            return new java.sql.Date(System.currentTimeMillis());
        }
        if (type == LocalDateTime.class) {
            return LocalDateTime.now();
        }
        return System.currentTimeMillis();
    }

    @Override
    public String getName() {
        return "当前系统时间";
    }
}