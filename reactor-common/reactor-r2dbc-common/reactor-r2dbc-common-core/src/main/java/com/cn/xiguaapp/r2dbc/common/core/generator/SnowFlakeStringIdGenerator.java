package com.cn.xiguaapp.r2dbc.common.core.generator;

import com.cn.xiguapp.common.core.gen.IDGenerator;
import com.cn.xiguaapp.r2dbc.orm.core.DefaultValueGenerator;
import com.cn.xiguaapp.r2dbc.orm.core.RuntimeDefaultValue;
import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.RDBColumnMetadata;

/**
 * @author xiguaapp
 * @desc 雪花算法id生成器
 * @see DefaultValueGenerator
 * @since 1.0 11:43
 */
public class SnowFlakeStringIdGenerator implements DefaultValueGenerator<RDBColumnMetadata> {
    @Override
    public String getSortId() {
        return Generators.SNOW_FLAKE;
    }

    @Override
    public RuntimeDefaultValue generate(RDBColumnMetadata metadata) {
        return IDGenerator.SNOW_FLAKE_STRING::generate;
    }

    @Override
    public String getName() {
        return "SnowFlake";
    }
}
