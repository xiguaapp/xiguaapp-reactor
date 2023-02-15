/*
 *
 *       Copyright (C) <2018-2028>  <@author: xiguaapp @date: 2020/12/7 下午5:50 >
 *
 *       Send: 1125698980@qq.com
 *
 *       This program is free software: you can redistribute it and/or modify
 *       it under the terms of the GNU General Public License as published by
 *       the Free Software Foundation, either version 3 of the License, or
 *       (at your option) any later version.
 *
 *       This program is distributed in the hope that it will be useful,
 *       but WITHOUT ANY WARRANTY; without even the implied warranty of
 *       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *       GNU General Public License for more details.
 *
 *       You should have received a copy of the GNU General Public License
 *       along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package com.cn.ykyoung.server.utils;

import aj.org.objectweb.asm.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ClassUtils;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;

/**
 * @author xiguaapp
 */
public class FieldUtil {

    private static final Logger log = LoggerFactory.getLogger(FieldUtil.class);

    private static final String CLASS_SUFFIX = ".class";

    private FieldUtil() {
    }

    /**
     * 判断这个字段是否是数字类型或字符串类型或枚举类型
     *
     * @param type 字段类型
     * @return true：是数字或字符串类型或枚举类型
     */
    public static boolean isNumberStringEnumType(Class<?> type) {
        if (type == String.class) {
            return true;
        }
        if (type.getGenericSuperclass() == Number.class) {
            return true;
        }
        if (type.isPrimitive()) {
            return true;
        }
        if (type.isEnum()) {
            return true;
        }
        return false;
    }

    /**
     * 获取指定类指定方法的参数名
     * @param clazz 要获取参数名的方法所属的类
     * @param method 要获取参数名的方法
     * @param index 参数索引，从0开始
     * @return 返回指定类指定方法的参数名，没有返回空字符串
     */
    public static String getMethodParameterName(Class<?> clazz, final Method method, int index) {
        String[] names = getMethodParameterNamesByAsm(clazz, method);
        if (names.length == 0) {
            return "";
        }
        return names[index];
    }

    /**
     * 获取指定类指定方法的参数名
     *
     * @param clazz  要获取参数名的方法所属的类
     * @param method 要获取参数名的方法
     * @return 按参数顺序排列的参数名列表，如果没有参数，则返回空数组
     */
    public static String[] getMethodParameterNamesByAsm(Class<?> clazz, final Method method) {
        final Class<?>[] parameterTypes = method.getParameterTypes();
        if (parameterTypes == null || parameterTypes.length == 0) {
            return new String[0];
        }
        final Type[] types = new Type[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            types[i] = Type.getType(parameterTypes[i]);
        }
        final String[] parameterNames = new String[parameterTypes.length];
        // 解决clazz对象是cglib对象导致空指针异常
        // 获取真实的class，如果是cglib类，则返回父类class
        Class<?> realClass = ClassUtils.getUserClass(clazz);
        String className = realClass.getName();
        int lastDotIndex = className.lastIndexOf('.');
        className = className.substring(lastDotIndex + 1) + CLASS_SUFFIX;
        InputStream is = realClass.getResourceAsStream(className);
        try {
            ClassReader classReader = new ClassReader(is);
            classReader.accept(new ClassVisitor(Opcodes.ASM6) {
                @Override
                public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
                    // 只处理指定的方法
                    Type[] argumentTypes = Type.getArgumentTypes(desc);
                    if (!method.getName().equals(name) || !Arrays.equals(argumentTypes, types)) {
                        return null;
                    }
                    return new MethodVisitor(Opcodes.ASM6) {
                        @Override
                        public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
                            int i = index - 1;
                            // 如果是静态方法，则第一就是参数
                            // 如果不是静态方法，则第一个是"this"，然后才是方法的参数
                            if (Modifier.isStatic(method.getModifiers())) {
                                i = index;
                            }
                            if (i >= 0 && i < parameterNames.length) {
                                parameterNames[i] = name;
                            }
                            super.visitLocalVariable(name, desc, signature, start,
                                    end, index);
                        }

                    };

                }
            }, 0);
        } catch (IOException e) {
            log.error("生成asm失败，oriClass:{}, realClass:{} method:{}", clazz.getName(), realClass.getName(), method.toGenericString(), e);
        }
        return parameterNames;
    }

}
