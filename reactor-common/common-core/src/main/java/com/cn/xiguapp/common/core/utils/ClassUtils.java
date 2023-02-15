/*
 *        Copyright (C) <2018-2028>  <@author: xiguaapp @date: @today>
 *        Send: 1125698980@qq.com
 *        This program is free software: you can redistribute it and/or modify
 *        it under the terms of the GNU General Public License as published by
 *        the Free Software Foundation, either version 3 of the License, or
 *        (at your option) any later version.
 *        This program is distributed in the hope that it will be useful,
 *        but WITHOUT ANY WARRANTY; without even the implied warranty of
 *        MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *        GNU General Public License for more details.
 *        You should have received a copy of the GNU General Public License
 *        along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.cn.xiguapp.common.core.utils;

import com.cn.xiguapp.common.core.time.DateTimeUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

public class ClassUtils {
    public static final Set<Class> basicClass = new HashSet();

    public ClassUtils() {
    }

    public static <T extends Annotation> T getAnnotation(Class<?> clazz, Class<T> annotation) {
        T ann = clazz.getAnnotation(annotation);
        if (ann != null) {
            return ann;
        } else {
            return clazz.getSuperclass() != Object.class ? getAnnotation(clazz.getSuperclass(), annotation) : ann;
        }
    }

    public static <T extends Annotation> T getAnnotation(Method method, Class<T> annotation) {
        T ann = method.getAnnotation(annotation);
        if (ann != null) {
            return ann;
        } else {
            Class clazz = method.getDeclaringClass();
            Class superClass = clazz.getSuperclass();
            if (superClass != Object.class) {
                try {
                    Method suMethod = superClass.getMethod(method.getName(), method.getParameterTypes());
                    return getAnnotation(suMethod, annotation);
                } catch (NoSuchMethodException var6) {
                    return null;
                }
            } else {
                return ann;
            }
        }
    }

    public static Class<?> getGenericTypeByType(ParameterizedType genType, int index) {
        Type[] params = genType.getActualTypeArguments();
        if (index < params.length && index >= 0) {
            Object res = params[index];
            if (res instanceof Class) {
                return (Class) res;
            } else {
                return res instanceof ParameterizedType ? (Class) ((ParameterizedType) res).getRawType() : null;
            }
        } else {
            return null;
        }
    }

    /**
     * 获取类/接口中泛型类型对象
     * @param clazz 类/接口
     * @param index 下标
     * @return 泛型类型对象
     */
    public static Class<?> getGenericType(Class clazz, int index) {
        List<Type> arrys = new ArrayList();
        arrys.add(clazz.getGenericSuperclass());
        arrys.addAll(Arrays.asList(clazz.getGenericInterfaces()));
        return (Class) arrys
                .stream()
                .filter(Objects::nonNull).map((type) -> clazz != Object.class && !(type instanceof ParameterizedType) ? getGenericType(clazz.getSuperclass(), index) : getGenericTypeByType((ParameterizedType) type, index)).filter(Objects::nonNull).filter((res) -> res != Object.class).findFirst().orElse(Object.class);
    }

    /**
     * 默认获取接口/实体类/基本类上泛型类型的对象
     * @param clazz 类/接口
     * @return 泛型类型对象
     */
    public static Class<?> getGenericType(Class clazz) {
        return getGenericType(clazz, 0);
    }

    public static boolean instanceOf(Class clazz, Class target) {
        if (clazz == null) {
            return false;
        } else if (clazz == target) {
            return true;
        } else {
            Class[] var2;
            int var3;
            int var4;
            Class aClass;
            if (target.isInterface()) {
                var2 = clazz.getInterfaces();
                var3 = var2.length;

                for (var4 = 0; var4 < var3; ++var4) {
                    aClass = var2[var4];
                    if (aClass == target) {
                        return true;
                    }
                }
            }

            if (clazz.getSuperclass() == target) {
                return true;
            } else {
                if (clazz.isInterface()) {
                    var2 = clazz.getInterfaces();
                    var3 = var2.length;

                    for (var4 = 0; var4 < var3; ++var4) {
                        aClass = var2[var4];
                        if (instanceOf(aClass, target)) {
                            return true;
                        }
                    }
                }

                return instanceOf(clazz.getSuperclass(), target);
            }
        }
    }

    public static final <T> Object cast(Object value, Class<T> type) {
        if (value == null) {
            return null;
        } else {
            Object newVal = null;
            if (instanceOf(value.getClass(), type)) {
                newVal = value;
            } else if (type != Integer.class && type != Integer.TYPE) {
                if (type != Double.class && type != Double.TYPE && type != Float.class && type != Float.TYPE) {
                    if (type != Long.class && type != Long.TYPE) {
                        if (type != Boolean.class && type != Boolean.TYPE) {
                            if (type == Date.class) {
                                newVal = DateTimeUtils.formatUnknownString2Date(value.toString());
                            } else if (type == String.class) {
                                if (value instanceof Date) {
                                    newVal = DateTimeUtils.format((Date) value, "yyyy-MM-dd HH:mm:ss");
                                } else {
                                    newVal = String.valueOf(value);
                                }
                            }
                        } else {
                            newVal = StringUtils.isTrue(value);
                        }
                    } else {
                        newVal = StringUtils.toLong(value);
                    }
                } else {
                    newVal = StringUtils.toDouble(value);
                }
            } else {
                newVal = StringUtils.toInt(value);
            }

            return newVal;
        }
    }

    public static boolean isBasicClass(Class clazz) {
        return basicClass.contains(clazz);
    }

    static {
        basicClass.add(Integer.TYPE);
        basicClass.add(Double.TYPE);
        basicClass.add(Float.TYPE);
        basicClass.add(Byte.TYPE);
        basicClass.add(Short.TYPE);
        basicClass.add(Character.TYPE);
        basicClass.add(String.class);
    }
}