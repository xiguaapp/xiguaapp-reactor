package com.cn.xiguaapp.r2dbc.orm.rdb.metadata;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.SQLType;

/**
 * @author xiguaapp
 * @desc jdbc数据类型
 * @since 1.0.0
 */
@AllArgsConstructor(staticName = "of")
public class JdbcDataType implements DataType {
    /**
     * sql类型
     */
    @Getter
    private SQLType sqlType;
    /**
     * java类型
     */
    @Getter
    private Class<?> javaType;

    /**
     * sqlType描述
     * @return String
     */
    @Override
    public String getName() {
        return sqlType.getName().toLowerCase();
    }

    /**
     * sqltype 描述id
     * @return String
     */
    @Override
    public String getId() {
        return sqlType.getName().toLowerCase();
    }


}
