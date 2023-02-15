package com.cn.xiguaapp.r2dbc.orm.rdb.codec;


import com.cn.xiguapp.common.core.time.DateFormatter;
import com.cn.xiguapp.common.core.utils.ClassUtils;
import com.cn.xiguapp.common.core.utils.StringUtils;
import com.cn.xiguaapp.r2dbc.orm.codec.ValueCodec;
import com.cn.xiguaapp.r2dbc.orm.rdb.executor.NullValue;

import java.math.BigDecimal;
import java.util.Date;
import java.util.function.Function;

/**
 * @author xiguaapp
 * @desc 数字转换
 * @since 1.0.0
 */
public class NumberValueCodec implements ValueCodec {

    private Function<Number, Object> converter;

    public NumberValueCodec(Function<Number, Object> converter) {
        this.converter = converter;
    }

    public NumberValueCodec(Class javaType) {
        if (javaType == int.class || javaType == Integer.class) {
            converter = Number::intValue;
        } else if (javaType == double.class || javaType == Double.class) {
            converter = Number::doubleValue;
        } else if (javaType == float.class || javaType == Float.class) {
            converter = Number::floatValue;
        } else if (javaType == long.class || javaType == Long.class) {
            converter = Number::longValue;
        } else if (javaType == byte.class || javaType == Byte.class) {
            converter = Number::byteValue;
        } else if (javaType == short.class || javaType == Short.class) {
            converter = Number::shortValue;
        } else if (javaType == boolean.class || javaType == Boolean.class) {
            converter = num -> num.byteValue() != 0;
        } else if (ClassUtils.instanceOf(javaType, Date.class)) {
            converter = num -> {
                try {
                    Date date = (Date) javaType.getDeclaredConstructor().newInstance();
                    date.setTime(num.longValue());
                    return date;
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            };
        } else {
            converter = num -> num;
        }
    }

    @Override
    public Object encode(Object value) {
        if (StringUtils.isNullOrEmpty(value)) {
            return null;
        }

        if (value instanceof Date) {
            return ((Date) value).getTime();
        } else if (!StringUtils.isNumber(value)) {
            //尝试转换字符格式的日期
            Date date = DateFormatter.fromString(String.valueOf(value));
            if (null != date) {
                value = date.getTime();
            }
        }
        if (value instanceof Number) {
            return converter.apply(((Number) value));
        }
        if (StringUtils.isNumber(value)) {
            return converter.apply(new BigDecimal(String.valueOf(value)));
        }
        if (Boolean.TRUE.equals(value)) {
            return converter.apply(1);
        }
        if (Boolean.FALSE.equals(value)) {
            return converter.apply(0);
        }
        if(value instanceof NullValue){
            return value;
        }
        throw new IllegalArgumentException("值" + value + "无法转换为数字");
    }


    @Override
    public Object decode(Object data) {
        if (data instanceof String) {
            if (StringUtils.isNumber(data)) {
                data = new BigDecimal(((String) data));
            }
        } else if (!StringUtils.isNumber(data)) {
            Date date = DateFormatter.fromString(String.valueOf(data));
            if (null != date) data = date.getTime();
        }
        if (data instanceof Number) {
            return converter.apply(((Number) data));
        }
        return data;
    }
}
