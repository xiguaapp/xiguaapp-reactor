package com.cn.xiguapp.common.core.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.FatalBeanException;
import org.springframework.util.Assert;

import java.beans.PropertyDescriptor;
import java.lang.reflect.*;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * @author xiguaapp
 * @Date 2020/9/24
 * @desc 对象拷贝
 */
public class BeansUtils<T> extends BeanUtils {
    /**
     * list与list之间相互转换
     * @param fromList 数据源
     * @param toElement 复制对象
     * @param after 接收对象
     * @param <T> 对象
     * @return List
     */
    public static <T> List<T> copyList(List<?> fromList, Supplier<T> toElement, Consumer<T> after) {
        if (fromList == null) {
            return Collections.emptyList();
        }
        return fromList.stream()
                .map(source -> {
                    T target = toElement.get();
                    BeanUtils.copyProperties(source, target);
                    after.accept(target);
                    return target;
                })
                .collect(Collectors.toList());
    }
    /**
     * 属性拷贝,第一个参数中的属性值拷贝到第二个参数中<br>
     * 注意:当第一个参数中的属性有null值时,不会拷贝进去
     *
     * @param source 源对象
     * @param target 目标对象
     * @throws BeansException
     */
    public static <T>void copyPropertiesIgnoreNull(Object source, T target)
            throws BeansException {
        Assert.notNull(source, "Source must not be null");
        Assert.notNull(target, "Target must not be null");
        Class<?> actualEditable = target.getClass();
        PropertyDescriptor[] targetPds = getPropertyDescriptors(actualEditable);
        for (PropertyDescriptor targetPd : targetPds) {
            Method writeMethod = targetPd.getWriteMethod();
            if (writeMethod != null) {
                PropertyDescriptor sourcePd = getPropertyDescriptor(
                        source.getClass(), targetPd.getName());
                if (sourcePd != null && sourcePd.getReadMethod() != null) {
                    try {
                        Method readMethod = sourcePd.getReadMethod();
                        if (!Modifier.isPublic(readMethod.getDeclaringClass()
                                .getModifiers())) {
                            readMethod.setAccessible(true);
                        }
                        Object value = readMethod.invoke(source);
                        // 这里判断value是否为空 当然这里也能进行一些特殊要求的处理
                        // 例如绑定时格式转换等等
                        if (value != null) {
                            if (!Modifier.isPublic(writeMethod
                                    .getDeclaringClass().getModifiers())) {
                                writeMethod.setAccessible(true);
                            }
                            writeMethod.invoke(target, value);
                        }
                    } catch (Throwable ex) {
                        throw new FatalBeanException(
                                "Could not copy properties from source to target field name mismatch:" + targetPd.getName(),
                                ex);
                    }
                }
            }
        }
    }

    /**
     * 复制并返回复制后的对象
     * @param from 对象数据来源
     * @param to  对象数据复制对象
     * @param <T> 对象实体
     * @return T
     */
    public static<T> T copy(Object from, T to){
        copyPropertiesIgnoreNull(from, to);
        return to;
    }

    /**
     * 复制并返回复制后的对象类
     * @param from 数据源
     * @param tClass 实体类
     * @param <T> 实体类
     * @return T t
     */
    public static <T> T copyOn(Object from,Class<T> tClass){
        T t = null;
        try {
            t = tClass.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return copy(from,t);
    }

    /**
     * 属性拷贝，把map中的值拷贝到target中去
     *
     * @param map    map对象
     * @param target 目标对象
     */
    public static <T>void copyPropertiesForMap(Map<String, Object> map, T target) {
        Assert.notNull(map, "map must not be null");
        Assert.notNull(target, "Target must not be null");
        Object pojo = mapToPojo(map, target.getClass());
        copyProperties(pojo, target);
    }

    /**
     * 将实体对象转换成Map
     *
     * @param pojo 实体类
     * @return 返回map
     */
    public static <T>Map<String, Object> pojoToMap(T pojo) {
        if (pojo == null) {
            return Collections.emptyMap();
        }
        String json = JSON.toJSONString(pojo);
        return JSON.parseObject(json);
    }


    /**
     * 将map对象转换成普通类
     *
     * @param <T>       普通类类型
     * @param map       map对象
     * @param pojoClass 普通类
     * @return 返回普通类
     */
    public static <T> T mapToPojo(Map<String, Object> map, Class<T> pojoClass) {
        return new JSONObject(map).toJavaObject(pojoClass);
    }

    /**
     * map集合转换成对象集合
     *
     * @param <T>       普通类类型
     * @param list      map集合
     * @param pojoClass 待转换的对象类型
     * @return 返回对象集合
     */
    public static <T> List<T> mapListToObjList(List<Map<String, Object>> list, Class<T> pojoClass) {
        if (list == null) {
            return Collections.emptyList();
        }
        List<T> retList = new ArrayList<>(list.size());
        for (Map<String, Object> map : list) {
            retList.add(mapToPojo(map, pojoClass));
        }
        return retList;
    }

    /**
     * 集合对象转换为List<Map>格式数据
     * @param l 集合对象
     * @param <T> 实体类
     * @see BeansUtils#pojoToMap(Object)
     * @return List<Map<String,Object>>
     */
    public static <T>List<Map<String,Object>> pojoListToMapList(List<T> l){
        if (l.size()<1){
            return Collections.emptyList();
        }
        return l.stream().filter(Objects::nonNull)
                .map(BeansUtils::pojoToMap)
                .collect(Collectors.toList());
    }

}
