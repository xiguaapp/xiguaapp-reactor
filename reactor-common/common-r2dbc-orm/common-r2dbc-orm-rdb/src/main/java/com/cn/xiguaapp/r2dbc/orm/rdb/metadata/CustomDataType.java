package com.cn.xiguaapp.r2dbc.orm.rdb.metadata;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.SQLType;

/**
 * @author xiguaapp
 * @desc 通用DataType
 * @since 1.0.0
 */
@Getter
@AllArgsConstructor(staticName = "of")
public class CustomDataType implements DataType {
    /**
     * id
     */
    private String id;
    /**
     * 名称
     */

    private String name;
    /**
     * sql类型
     */
    private SQLType sqlType;
    /**
     * java类型
     */
    private Class<?> javaType;

}
