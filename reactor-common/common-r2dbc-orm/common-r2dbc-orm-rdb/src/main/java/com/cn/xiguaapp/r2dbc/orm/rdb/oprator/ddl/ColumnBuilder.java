package com.cn.xiguaapp.r2dbc.orm.rdb.oprator.ddl;


import com.cn.xiguaapp.r2dbc.orm.core.DefaultValue;
import com.cn.xiguaapp.r2dbc.orm.core.RuntimeDefaultValue;
import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.DataType;
import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.NativeSqlDefaultValue;
import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.RDBColumnMetadata;

import java.math.BigDecimal;
import java.sql.JDBCType;
import java.util.Date;
import java.util.function.Consumer;

/**
 * @author xiguaapp
 */
public interface ColumnBuilder {
    ColumnBuilder custom(Consumer<RDBColumnMetadata> consumer);

    ColumnBuilder name(String name);

    ColumnBuilder alias(String name);

    ColumnBuilder dataType(String dataType);

    ColumnBuilder type(String typeId);

    ColumnBuilder type(DataType type);

    ColumnBuilder comment(String comment);

    ColumnBuilder notNull();

    ColumnBuilder primaryKey();

    ColumnBuilder columnDef(String def);

    ColumnBuilder defaultValue(DefaultValue value);

    default ColumnBuilder type(JDBCType jdbcType, Class type) {
        return type(DataType.jdbc(jdbcType, type));
    }

    default ColumnBuilder varchar(int length) {
        return type(JDBCType.VARCHAR, String.class).length(length);
    }

    default ColumnBuilder defaultValueNative(String defaultSql) {
        return defaultValue(NativeSqlDefaultValue.of(defaultSql));
    }

    default ColumnBuilder defaultValueRuntime(RuntimeDefaultValue value) {
        return defaultValue(value);
    }

    default ColumnBuilder number(int precision, int scale) {
        return type(JDBCType.NUMERIC, BigDecimal.class).length(precision, scale);
    }

    default ColumnBuilder number(int len) {
        return type(JDBCType.NUMERIC, Long.class).length(len, 0);
    }

    default ColumnBuilder text() {
        return type(JDBCType.LONGVARCHAR, String.class);
    }

    default ColumnBuilder clob() {
        return type(JDBCType.CLOB, String.class);
    }

    default ColumnBuilder integer() {
        return type(JDBCType.INTEGER, Integer.class);
    }

    default ColumnBuilder bigint() {
        return type(JDBCType.BIGINT, Long.class);
    }

    default ColumnBuilder tinyint() {
        return type(JDBCType.TINYINT, Byte.class);
    }

    default ColumnBuilder datetime() {
        return type(JDBCType.TIMESTAMP, Date.class);
    }

    ColumnBuilder property(String propertyName, Object value);

    ColumnBuilder length(int len);

    ColumnBuilder length(int precision, int scale);

    TableBuilder commit();
}
