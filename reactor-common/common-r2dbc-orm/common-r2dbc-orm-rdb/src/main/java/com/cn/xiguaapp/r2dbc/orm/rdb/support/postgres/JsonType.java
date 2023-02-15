package com.cn.xiguaapp.r2dbc.orm.rdb.support.postgres;

import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.DataType;
import lombok.Getter;

import java.sql.JDBCType;
import java.sql.SQLType;


/**
 * @author xiguaapp
 */
@Getter
public class JsonType implements DataType {

    public static JsonType INSTANCE = new JsonType();

    @Override
    public Class<?> getJavaType() {
        return String.class;
    }

    @Override
    public String getId() {
        return "json";
    }

    @Override
    public String getName() {
        return "json";
    }

    @Override
    public SQLType getSqlType() {
        return JDBCType.CLOB;
    }


}
