package com.cn.xiguaapp.r2dbc.orm.rdb.metadata;


import com.cn.xiguaapp.r2dbc.orm.rdb.utils.DataTypeUtils;

import java.sql.JDBCType;
import java.sql.SQLType;
import java.util.function.Function;

/**
 * @author xiguaapp
 * @see JdbcDataType
 * @see CustomDataType
 * @desc 验证数据类型与数据库数据类型
 */
public interface DataType {

    String getId();

    String getName();

    /**
     * 获取sql数据类型
     * @return
     */
    SQLType getSqlType();

    /**
     * 获取java数据类型
     * @return class
     */
    Class<?> getJavaType();

    /**
     * 校验sql数据类型是否与jdbc类型一致
     * @return boolean
     */
    default boolean isScaleSupport() {
        return getSqlType() == JDBCType.DECIMAL ||
                getSqlType() == JDBCType.DOUBLE ||
                getSqlType() == JDBCType.NUMERIC ||
                getSqlType() == JDBCType.FLOAT;
    }

    /**
     * 校验数据类型且校验字符长度属于那类数据类型
     * @return boolean
     */
    default boolean isLengthSupport() {
        return isScaleSupport() ||
                getSqlType() == JDBCType.VARCHAR ||
                getSqlType() == JDBCType.CHAR||
                getSqlType() == JDBCType.NVARCHAR
                ;
    }

    /**
     * 判断当前类型是否为数字
     * @return boolean
     */
    default boolean isNumber(){
        return DataTypeUtils.typeIsNumber(this);
    }

    /**
     * 组装参数为dataType类型
     * @see CustomDataType
     * @param id id
     * @param name 名称
     * @param sqlType sql类型
     * @param javaType java类型
     * @return dataType
     */
    static DataType custom(String id, String name, SQLType sqlType, Class<?> javaType) {
        return CustomDataType.of(id, name, sqlType, javaType);
    }

    /**
     * 将jdbc组装Datype
     * @see JdbcDataType
     * @param jdbcType jdbc数据类型
     * @param javaType Java数据类型
     * @return DataType
     */
    static DataType jdbc(JDBCType jdbcType, Class<?> javaType) {
        return JdbcDataType.of(jdbcType, javaType);
    }

    /**
     * 组装dataType类型
     * @param type type
     * @param builder
     * @return dataType
     */
    static DataType builder(DataType type, Function<RDBColumnMetadata, String> builder) {
        return DataTypeBuilderSupport.of(type, builder);
    }
}
