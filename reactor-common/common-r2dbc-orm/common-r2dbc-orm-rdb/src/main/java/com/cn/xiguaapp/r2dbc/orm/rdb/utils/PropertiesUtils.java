package com.cn.xiguaapp.r2dbc.orm.rdb.utils;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.*;

/**
 * @author xiguaapp
 * @desc object转list、某实体类是否存在当前字段工具类
 * @since  1.0.0
 */
@Slf4j
public class PropertiesUtils {

    /**
     * 将object转换为集合
     * @param object
     * @return
     */
    @SuppressWarnings("all")
    public static List<Object> convertList(Object object) {
        if (object == null) {
            return Collections.emptyList();
        }
        if (object instanceof Object[]) {
            return Arrays.asList(((Object[]) object));
        }
        if (object instanceof Collection) {
            return new ArrayList<>(((Collection) object));
        }

        return Arrays.asList(object);
    }

    /**
     * 查询某实体类中是否存在该字段
     * @param clazz
     * @param fieldName
     * @return
     */
    public static Optional<Field> getPropertyField(Class clazz,String fieldName){
        Field field = null;
        Class tmp = clazz;
        do {
            try {
                field = tmp.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                tmp = tmp.getSuperclass();
                if (tmp == null || tmp == Object.class) {
                    break;
                }
            }
        } while (field == null);

        return Optional.ofNullable(field);
    }
}
