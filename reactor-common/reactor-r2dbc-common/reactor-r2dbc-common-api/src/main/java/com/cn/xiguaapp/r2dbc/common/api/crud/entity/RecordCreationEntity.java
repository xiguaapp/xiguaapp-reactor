package com.cn.xiguaapp.r2dbc.common.api.crud.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDateTime;

/**
 * @author xiguaapp
 * @desc
 *      记录创建信息的实体类, 包括创建人和创建时间。
 *      此实体类与行级权限控制相关联:只能操作自己创建的数据
 * @since 1.0 22:26
 */
public interface RecordCreationEntity extends Entity {
    String getCreateById();

    void setCreateById(String createById);

    LocalDateTime getCreateTime();

    void setCreateTime(LocalDateTime createTime);

    default void setCreateByName(String name) {

    }

    default void setCreateTimeNow() {
        setCreateTime(LocalDateTime.now());
    }

    @JsonIgnore
    default String getCreateByIdProperty() {
        return "createById";
    }
}
