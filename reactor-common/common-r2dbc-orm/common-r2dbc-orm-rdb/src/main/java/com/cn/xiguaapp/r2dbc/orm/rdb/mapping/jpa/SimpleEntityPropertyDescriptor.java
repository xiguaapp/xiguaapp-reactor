package com.cn.xiguaapp.r2dbc.orm.rdb.mapping.jpa;

import com.cn.xiguapp.common.core.annotations.AnnotationUtils;
import com.cn.xiguaapp.r2dbc.orm.rdb.mapping.EntityPropertyDescriptor;
import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.RDBColumnMetadata;
import com.cn.xiguaapp.r2dbc.orm.rdb.utils.PropertiesUtils;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Optional;
import java.util.Set;

/**
 * @author xiguaapp
 * @desc 基础数据库表配置
 */
@AllArgsConstructor(staticName = "of")
public class SimpleEntityPropertyDescriptor implements EntityPropertyDescriptor {
    /**
     * 实体类
     */
    private Class entityType;
    /**
     * 属性名
     */
    private String propertyName;
    /**
     * 类型
     */
    private Class propertyType;
    /**
     * 数据库字段封装信息
     */
    private RDBColumnMetadata column;
    /**
     * 属性描述
     */
    private PropertyDescriptor descriptor;

    public static EntityPropertyDescriptor of(PropertyDescriptor descriptor, RDBColumnMetadata column) {
        return SimpleEntityPropertyDescriptor.of(
                descriptor.getReadMethod().getDeclaringClass(),
                descriptor.getName(),
                descriptor.getPropertyType(),
                column,
                descriptor
        );
    }

    @Override
    public String getPropertyName() {
        return propertyName;
    }

    @Override
    @SneakyThrows
    public Field getField() {
        return PropertiesUtils.getPropertyField(entityType, descriptor.getName()).orElseThrow(() -> new NoSuchFieldException("no such field " + propertyName + " in " + entityType));
    }

    @Override
    public Class getPropertyType() {
        return propertyType;
    }

    @Override
    public RDBColumnMetadata getColumn() {
        return column;
    }

    @Override
    public <T extends Annotation> Optional<T> findAnnotation(Class<T> tClass) {

        return Optional.ofNullable(AnnotationUtils.getAnnotation(entityType, descriptor, tClass));
    }

    @Override
    public Set<Annotation> getAnnotations() {
        return AnnotationUtils.getAnnotations(entityType, descriptor);
    }
}
