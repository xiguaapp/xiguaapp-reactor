/*
 * Copyright 2019 http://www.hswebframework.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package com.cn.xiguapp.common.core.annotations;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

/**
 * @author xiguaapp
 * @desc annotation工具
 */
public final class AnnotationUtils {

    private AnnotationUtils() {
    }
    public static Set<Annotation> getAnnotations(Class entityClass, PropertyDescriptor descriptor) {
        Set<Annotation> annotations = new HashSet<>();
        Set<Class<? extends Annotation>> types = new HashSet<>();

        Consumer<Annotation[]> annoConsumer =(ann)->{
            for (Annotation annotation : ann) {
                if(!types.contains(annotation.getClass())){
                    annotations.add(annotation);
                }
                types.add(annotation.annotationType());
            }
        };

        //先获取方法
        Method read = descriptor.getReadMethod(),
                write = descriptor.getWriteMethod();
        if (read != null) {
            annoConsumer.accept(read.getAnnotations());
        }
        if (write != null) {
            annoConsumer.accept(write.getAnnotations());
        }

        //获取属性
        while (true) {
            try {
                Field field = entityClass.getDeclaredField(descriptor.getName());
                annoConsumer.accept(field.getAnnotations());
                break;
            } catch (NoSuchFieldException e) {
                entityClass = entityClass.getSuperclass();
                if (entityClass == null || entityClass == Object.class) {
                    break;
                }
            }
        }


        return annotations;
    }

    public static <T extends Annotation> T getAnnotation(Class entityClass, PropertyDescriptor descriptor, Class<T> type) {
        T ann = null;
        if (descriptor == null) {
            return null;
        }
        //先获取方法
        Method read = descriptor.getReadMethod(),
                write = descriptor.getWriteMethod();
        if (read != null) {
            ann = getAnnotation(read, type);
        }
        if (null == ann && write != null) {
            ann = getAnnotation(write, type);
        }
        //获取属性
        while (ann == null) {
            try {
                Field field = entityClass.getDeclaredField(descriptor.getName());
                ann = field.getAnnotation(type);
                break;
            } catch (NoSuchFieldException e) {
                entityClass = entityClass.getSuperclass();
                if (entityClass == null || entityClass == Object.class) {
                    break;
                }
            }
        }

        return ann;
    }

    public static <T extends Annotation> T getAnnotation(Class<?> clazz, Class<T> annotation) {
        T ann = clazz.getAnnotation(annotation);

        while (ann == null) {
            clazz = clazz.getSuperclass();
            if (clazz == null || clazz == Object.class) {
                break;
            }
            ann = clazz.getAnnotation(annotation);
        }

        return ann;
    }


    public static <T extends Annotation> T getAnnotation(Method method, Class<T> annotation) {
        T ann = method.getAnnotation(annotation);

        Class<?> clazz = method.getDeclaringClass();

        while (ann == null) {
            clazz = clazz.getSuperclass();
            if (clazz == null || clazz == Object.class) {
                break;
            }
            try {
                //父类方法
                Method suMethod = clazz.getMethod(method.getName(), method.getParameterTypes());
                ann = suMethod.getAnnotation(annotation);
            } catch (NoSuchMethodException e) {
                return null;
            }
        }
        return ann;
    }

    public static <T extends Annotation> T findMethodAnnotation(Class targetClass, Method method, Class<T> annClass) {
        Method m = method;
        T a = org.springframework.core.annotation.AnnotationUtils.findAnnotation(m, annClass);
        if (a != null) {
            return a;
        }
        m = ClassUtils.getMostSpecificMethod(m, targetClass);
        a = org.springframework.core.annotation.AnnotationUtils.findAnnotation(m, annClass);
        if (a == null) {
            List<Class> supers = new ArrayList<>(Arrays.asList(targetClass.getInterfaces()));
            if (targetClass.getSuperclass() != Object.class) {
                supers.add(targetClass.getSuperclass());
            }

            for (Class aClass : supers) {
                if(aClass==null){
                    continue;
                }
                AtomicReference<Method> methodRef = new AtomicReference<>();
                ReflectionUtils.doWithMethods(aClass, im -> {
                    if (im.getName().equals(method.getName()) && im.getParameterCount() == method.getParameterCount()) {
                        methodRef.set(im);
                    }
                });

                if (methodRef.get() != null) {
                    a = findMethodAnnotation(aClass, methodRef.get(), annClass);
                    if (a != null) {
                        return a;
                    }
                }
            }
        }
        return a;
    }

    public static <T extends Annotation> T findAnnotation(Class targetClass, Class<T> annClass) {
        return org.springframework.core.annotation.AnnotationUtils.findAnnotation(targetClass, annClass);
    }

    public static <T extends Annotation> T findAnnotation(Class targetClass, Method method, Class<T> annClass) {
        T a = findMethodAnnotation(targetClass, method, annClass);
        if (a != null) {
            return a;
        }
        return findAnnotation(targetClass, annClass);
    }

    public static <T extends Annotation> T findAnnotation(JoinPoint pjp, Class<T> annClass) {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method m = signature.getMethod();
        Class<?> targetClass = pjp.getTarget().getClass();
        return findAnnotation(targetClass, m, annClass);
    }

    public static String getMethodBody(JoinPoint pjp) {
        StringBuilder methodName = new StringBuilder(pjp.getSignature().getName()).append("(");
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        String[] names = signature.getParameterNames();
        Class[] args = signature.getParameterTypes();
        for (int i = 0, len = args.length; i < len; i++) {
            if (i != 0) {
                methodName.append(",");
            }
            methodName.append(args[i].getSimpleName()).append(" ").append(names[i]);
        }
        return methodName.append(")").toString();
    }

    public static Map<String, Object> getArgsMap(JoinPoint pjp) {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Map<String, Object> args = new LinkedHashMap<>();
        String names[] = signature.getParameterNames();
        for (int i = 0, len = names.length; i < len; i++) {
            args.put(names[i], pjp.getArgs()[i]);
        }
        return args;
    }
}