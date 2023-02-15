package com.cn.xiguaapp.r2dbc.orm.rdb.mapping.parser;

import com.cn.xiguaapp.r2dbc.orm.rdb.mapping.EntityPropertyDescriptor;
import com.cn.xiguaapp.r2dbc.orm.rdb.mapping.annotation.ColumnType;
import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.DataType;
import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.RDBColumnMetadata;
import lombok.SneakyThrows;
public class DefaultDataTypeResolver implements DataTypeResolver {

    public static final DefaultDataTypeResolver INSTANCE = new DefaultDataTypeResolver();

    @SneakyThrows
    protected DataType getDataTypeInstance(Class<? extends DataType> type) {
        return type.getDeclaredConstructor().newInstance();
    }

    @Override
    public DataType resolve(EntityPropertyDescriptor descriptor) {

        return descriptor.findAnnotation(ColumnType.class)
                .map(type -> {
                    RDBColumnMetadata column = descriptor.getColumn();
                    Class<?> javaType = type.javaType() == Void.class ? descriptor.getPropertyType() : type.javaType();

                    if (!type.typeId().isEmpty()) {
                        return column == null ?
                                DataType.custom(type.typeId(), type.name(), type.jdbcType(), javaType)
                                :
                                column.getDialect().convertDataType(type.typeId());
                    } else if (type.type() != DataType.class) {
                        return getDataTypeInstance(type.type());
                    } else {

                        if (type.javaType() == Void.class && null != column) {
                            return column.getDialect()
                                    .convertDataType(type.jdbcType().getName());
                        }
                        return DataType.jdbc(type.jdbcType(), javaType);
                    }
                }).orElse(null);
    }
}
