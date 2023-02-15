package com.cn.xiguaapp.r2dbc.orm.rdb.mapping.annotation;


import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.DataType;
import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.JdbcDataType;
import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.RDBColumnMetadata;

import java.lang.annotation.*;
import java.sql.JDBCType;

/**
 * @author xiguaapp
 * @see DataType
 * @see RDBColumnMetadata
 */
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface ColumnType {

    /**
     * @return 类型标识
     * @see DataType#getId()
     */
    String typeId() default "";

    String name() default "";

    /**
     * @return JDBCType
     * @see DataType#getSqlType()
     */
    JDBCType jdbcType() default JDBCType.VARCHAR;

    /**
     * @return 自定义java类型
     * @see RDBColumnMetadata#getJavaType()
     */
    Class javaType() default Void.class;

    /**
     * @return DataType class
     * @see DataType
     * @see JdbcDataType
     * @see RDBColumnMetadata#getJavaType()
     */
    Class<? extends DataType> type() default DataType.class;

}
