package com.cn.xiguaapp.r2dbc.common.core;

import com.cn.xiguapp.common.core.utils.ClassUtils;

/**
 * @author xiguaapp
 * @desc
 * @since 1.0 17:25
 */
public class FindClass {

    /**
     * 获取当前实体类类型
     * @return 当前类泛型所属类
     */
    public static <T>Class<T> findClass(Class c){
        return findClass(c,0);
    }

    /**
     * 获取id类型
     * @return 当前类泛型id所属类型
     */
    public static <T>Class<T>findClass(Class c,int index){
        return (Class<T>) ClassUtils.getGenericType(c,index);
    }

}
