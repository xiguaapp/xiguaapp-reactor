package com.cn.xiguaapp.r2dbc.orm.rdb.test.utils;

import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.DataType;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.JDBCType;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static com.cn.xiguaapp.r2dbc.orm.rdb.utils.DataTypeUtils.typeIsDate;
import static com.cn.xiguaapp.r2dbc.orm.rdb.utils.DataTypeUtils.typeIsNumber;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@Slf4j
public class DataTypeUtilsTest {

    /**
     * 数字类型校验
     */
    @Test
    public void testNumberByJavaType() {

        assertFalse(typeIsNumber(null));

        assertTrue(typeIsNumber(DataType.jdbc(JDBCType.OTHER, byte.class)));
        assertTrue(typeIsNumber(DataType.jdbc(JDBCType.OTHER, short.class)));
        assertTrue(typeIsNumber(DataType.jdbc(JDBCType.OTHER, int.class)));
        assertTrue(typeIsNumber(DataType.jdbc(JDBCType.OTHER, float.class)));
        assertTrue(typeIsNumber(DataType.jdbc(JDBCType.OTHER, double.class)));
        assertTrue(typeIsNumber(DataType.jdbc(JDBCType.OTHER, long.class)));

        assertTrue(typeIsNumber(DataType.jdbc(JDBCType.OTHER, Byte.class)));
        assertTrue(typeIsNumber(DataType.jdbc(JDBCType.OTHER, Short.class)));
        assertTrue(typeIsNumber(DataType.jdbc(JDBCType.OTHER, Integer.class)));
        assertTrue(typeIsNumber(DataType.jdbc(JDBCType.OTHER, Float.class)));
        assertTrue(typeIsNumber(DataType.jdbc(JDBCType.OTHER, Double.class)));
        assertTrue(typeIsNumber(DataType.jdbc(JDBCType.OTHER, Long.class)));

        assertTrue(typeIsNumber(DataType.jdbc(JDBCType.OTHER, BigDecimal.class)));
        assertTrue(typeIsNumber(DataType.jdbc(JDBCType.OTHER, BigInteger.class)));

        assertFalse(typeIsNumber(DataType.jdbc(JDBCType.OTHER, String.class)));
        log.info("无异常，默认校验成功");

    }

    /**
     * 数字类型校验
     */
    @Test
    public void testNumberByJdbcType() {
        assertFalse(typeIsNumber(null));

        assertTrue(typeIsNumber(DataType.jdbc(JDBCType.NUMERIC, String.class)));
        assertTrue(typeIsNumber(DataType.jdbc(JDBCType.INTEGER, String.class)));
        assertTrue(typeIsNumber(DataType.jdbc(JDBCType.BIGINT, String.class)));
        assertTrue(typeIsNumber(DataType.jdbc(JDBCType.DOUBLE, String.class)));
        assertTrue(typeIsNumber(DataType.jdbc(JDBCType.FLOAT, String.class)));
        assertTrue(typeIsNumber(DataType.jdbc(JDBCType.DECIMAL, String.class)));
        assertTrue(typeIsNumber(DataType.jdbc(JDBCType.BIT, String.class)));

        assertFalse(typeIsNumber(DataType.jdbc(JDBCType.DATE, String.class)));
        log.info("无异常，默认校验成功");


    }

    /**
     * 时间校验
     */
    @Test
    public void testDateByJdbcType() {
        assertFalse(typeIsDate(null));

        assertTrue(typeIsDate(DataType.jdbc(JDBCType.DATE, String.class)));
        assertTrue(typeIsDate(DataType.jdbc(JDBCType.TIME, String.class)));
        assertTrue(typeIsDate(DataType.jdbc(JDBCType.TIMESTAMP, String.class)));
        assertTrue(typeIsDate(DataType.jdbc(JDBCType.TIME_WITH_TIMEZONE, String.class)));
        assertTrue(typeIsDate(DataType.jdbc(JDBCType.TIMESTAMP_WITH_TIMEZONE, String.class)));

        assertFalse(typeIsNumber(DataType.jdbc(JDBCType.VARCHAR, String.class)));
        log.info("无异常，默认校验成功");

    }

    /**
     * 时间校验
     */
    @Test
    public void testDateByJavaType() {
        assertFalse(typeIsDate(null));

        assertTrue(typeIsDate(DataType.jdbc(JDBCType.VARCHAR, Date.class)));
        assertTrue(typeIsDate(DataType.jdbc(JDBCType.VARCHAR, java.util.Date.class)));
        assertTrue(typeIsDate(DataType.jdbc(JDBCType.VARCHAR, Timestamp.class)));
        assertTrue(typeIsDate(DataType.jdbc(JDBCType.VARCHAR, LocalDate.class)));
        assertTrue(typeIsDate(DataType.jdbc(JDBCType.VARCHAR, LocalTime.class)));
        assertTrue(typeIsDate(DataType.jdbc(JDBCType.VARCHAR, LocalDateTime.class)));

        assertFalse(typeIsNumber(DataType.jdbc(JDBCType.VARCHAR, String.class)));
        log.info("无异常，默认校验成功");

    }
}