package com.cn.xiguaapp.r2dbc.common.core.generator;

import com.cn.xiguapp.common.core.gen.IDGenerator;
import com.cn.xiguaapp.r2dbc.orm.core.DefaultValueGenerator;
import com.cn.xiguaapp.r2dbc.orm.core.RuntimeDefaultValue;
import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.RDBColumnMetadata;

/**
 * @author xiguaapp
 * @desc md5 id生成器
 * @see DefaultValueGenerator
 * @since 1.0 11:42
 */
public class MD5Generator implements DefaultValueGenerator<RDBColumnMetadata> {
    @Override
    public String getSortId() {
        return Generators.MD5;
    }

    @Override
    public RuntimeDefaultValue generate(RDBColumnMetadata metadata) {
        return IDGenerator.MD5::generate;
    }

    @Override
    public String getName() {
        return "MD5";
    }
}
