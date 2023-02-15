/*
 *
 *       Copyright (C) <2018-2028>  <@author: xiguaapp @date: 2020/12/7 下午5:52 >
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

package com.cn.ykyoung.server.common.param;


import aj.org.objectweb.asm.*;
import com.cn.ykyoung.server.utils.FieldUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 包装单个参数
 * <code>
 *     public class WrapperParam0id implements SingleValue {
 *         public String id;
 *
 *         public Object fetchValue() {
 *             return this.id;
 *         }
 *     }
 * </code>
 *
 * @author xiguaapp
 */
public class SingleParameterClassCreator implements Opcodes {

    private static final Logger logger = LoggerFactory.getLogger(SingleParameterClassCreator.class);

    private static final String FOLDER_END_CHAR = "/";

    private static final String CLASS_FILE_SUFFIX = ".class";

    private static final String WRAPPER_PARAM = "WrapperParam";

    private static final String PKG = "com/gitee/sop/servercommon/gen/";
    private static final String OBJECT = "java/lang/Object";

    private static final ParameterClassLoader CLASS_LOADER = new ParameterClassLoader();

    private static final AtomicInteger i = new AtomicInteger();

    // 生成class文件的保存路径
    private String savePath;

    /**
     * 生成一个类，里面指放这个字段
     *
     * @param parameter 字段，只能是基本类型或字符串类型
     * @return 如果不是基本类型或字符串类型，返回null
     */
    public Class<?> create(Parameter parameter, String paramName) {
        Class<?> paramType = parameter.getType();
        if (!FieldUtil.isNumberStringEnumType(paramType)) {
            return null;
        }
        /* *******************************class********************************************** */
        // 创建一个ClassWriter, 以生成一个新的类
        ClassWriter classWriter = new ClassWriter(0);
        // 类名
        String className = WRAPPER_PARAM + i.incrementAndGet() + paramName;
        // 类路径名：com/gitee/sop/service/gen/WrapperParam
        String classpathName = PKG + className;
        classWriter.visit(V1_8, ACC_PUBLIC, classpathName, null, OBJECT, null);

        /* ********************************constructor********************************************* */
        MethodVisitor methodVisitor = classWriter.visitMethod(ACC_PUBLIC, "<init>", "()V", null,
                null);
        methodVisitor.visitVarInsn(ALOAD, 0);
        methodVisitor.visitMethodInsn(INVOKESPECIAL, OBJECT, "<init>", "()V", false);
        methodVisitor.visitInsn(RETURN);
        methodVisitor.visitMaxs(1, 1);
        methodVisitor.visitEnd();


        /* ************************************parameter***************************************** */
        // 生成String name字段, Ljava/lang/String;
        Type type = Type.getType(paramType);
        FieldVisitor fieldVisitor = classWriter.visitField(ACC_PUBLIC, paramName, type.getDescriptor(), null, null);
        // 生成验证注解
        Annotation[] annotations = parameter.getAnnotations();
        for (Annotation annotation : annotations) {
            ValidationAnnotationDefinition validationAnnotationDefinition = ValidationAnnotationDefinitionFactory.build(annotation);
            if (validationAnnotationDefinition == null) {
                continue;
            }
            Class<?> annoClass = validationAnnotationDefinition.getAnnotationClass();
            Type annoType = Type.getType(annoClass);
            AnnotationVisitor annotationVisitor = fieldVisitor.visitAnnotation(annoType.getDescriptor(), true);
            Map<String, Object> properties = validationAnnotationDefinition.getProperties();
            if (properties != null) {
                try {
                    Set<Map.Entry<String, Object>> entrySet = properties.entrySet();
                    for (Map.Entry<String, Object> entry : entrySet) {
                        Object val = entry.getValue();
                        if (val != null) {
                            // 设置枚举值
                            if (val.getClass().isEnum()) {
                                Type eType = Type.getType(val.getClass());
                                annotationVisitor.visitEnum(entry.getKey(), eType.getDescriptor(), val.toString());
                            } else if (val instanceof Class<?>) {
                                // val是Class类型
                                Type vType = Type.getType((Class<?>) val);
                                annotationVisitor.visit(entry.getKey(), vType);
                            } else {
                                annotationVisitor.visit(entry.getKey(), val);
                            }
                        }
                    }
                } catch (Exception e) {
                    logger.error("ASM生成注解出错", e);
                }
            }
            // 结束生成注解
            annotationVisitor.visitEnd();
            logger.debug("ASM生成参数注解，参数：{}，注解：{}，注解属性：{}", paramName, annoClass.getName(), properties);
        }
        fieldVisitor.visitEnd();

        classWriter.visitEnd();

        /* **********************************generate and load******************************************* */
        byte[] code = classWriter.toByteArray();

        if (StringUtils.isNotBlank(savePath)) {
            if (!savePath.endsWith(FOLDER_END_CHAR)) {
                savePath = savePath + FOLDER_END_CHAR;
            }
            this.writeClassFile(code, savePath + className + CLASS_FILE_SUFFIX);
        }

        Class<?> clazz = CLASS_LOADER.defineClass(code);
        logger.debug("生成参数包装类：{}，包装参数名：{}，参数类型：{}", clazz.getName(), paramName, paramType);
        return clazz;
    }

    protected void writeClassFile(byte[] code, String filepath) {
        // 将二进制流写到本地磁盘上
        try {
            FileUtils.writeByteArrayToFile(new File(filepath), code);
        } catch (IOException e) {
            logger.error("写文件错误，filepath:" + filepath, e);
        }
    }

    public void setSavePath(String savePath) {
        this.savePath = savePath;
    }


    // 自定义类加载器
    static class ParameterClassLoader extends ClassLoader {
        public ParameterClassLoader() {
            /*
                指定父加载器，不指定默认为AppClassLoader
                springboot启动会使用自带的org.springframework.boot.loader.LaunchedURLClassLoader
                如果不指定，会出现加载器不一致，导致ASM生成的class获取不到字段的注解。
                因此ASM生成的class必须使用当前ClassLoader进行生成。
             */
            super(Thread.currentThread().getContextClassLoader());
        }

        /**
         * 加载class
         *
         * @param clazz 字节码
         * @return
         */
        public Class<?> defineClass(byte[] clazz) {
            return this.defineClass(null, clazz, 0, clazz.length);
        }
    }

}
