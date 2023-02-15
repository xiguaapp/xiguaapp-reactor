package com.cn.xiguaapp.r2dbc.orm.rdb.metadata;

import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.dialect.DataTypeBuilder;
import lombok.AllArgsConstructor;

import java.sql.SQLType;
import java.util.function.Function;

/**
 * @author xiguaapp
 * @desc 组装数据类型
 * @since 1.0.0
 */
@AllArgsConstructor(staticName = "of")
public class DataTypeBuilderSupport implements DataType, DataTypeBuilder {

    private DataType parent;

    private Function<RDBColumnMetadata, String> builder;

    @Override
    public String getId() {
        return parent.getId();
    }

    @Override
    public String getName() {
        return parent.getName();
    }

    @Override
    public SQLType getSqlType() {
        return parent.getSqlType();
    }

    @Override
    public Class<?> getJavaType() {
        return parent.getJavaType();
    }

    @Override
    public String createColumnDataType(RDBColumnMetadata columnMetaData) {
        return builder.apply(columnMetaData);
    }
}
