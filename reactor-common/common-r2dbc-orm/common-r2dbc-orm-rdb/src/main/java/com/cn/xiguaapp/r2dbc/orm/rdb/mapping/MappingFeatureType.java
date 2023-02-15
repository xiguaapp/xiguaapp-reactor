package com.cn.xiguaapp.r2dbc.orm.rdb.mapping;

import com.cn.xiguaapp.r2dbc.orm.feature.FeatureType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MappingFeatureType implements FeatureType {

    /**
     * @see EntityColumnMapping
     */
    columnPropertyMapping("列与属性映射关系"),

    /**
     * @see EntityManager
     */
    entityManager("实体类管理器"),

    /**
     * @see EntityPropertyDescriptor
     */
    propertyDescriptor("属性描述器")
    ;

    private String name;

    @Override
    public String getId() {
        return name();
    }

    public String createFeatureId(Class type) {
        return getId().concat(":").concat(type.getName());
    }
}
