package com.cn.xiguaapp.r2dbc.orm.rdb.support.postgres;

import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.DataType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.JDBCType;
import java.sql.SQLType;

/**
 * @author xiguaapp
 */
@Getter
@AllArgsConstructor
public class JsonbType implements DataType {
    public static JsonbType INSTANCE = new JsonbType();

    @Override
    public Class<?> getJavaType() {
        return String.class;
    }

    @Override
    public String getId() {
        return "jsonb";
    }

    @Override
    public String getName() {
        return "jsonb";
    }

    @Override
    public SQLType getSqlType() {
        return JDBCType.CLOB;
    }
}
