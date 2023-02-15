package com.cn.xiguaapp.r2dbc.common.api.crud.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDateTime;

/**
 * @author xiguaapp
 * @desc 记录修改信息的实体类,包括修改人和修改时间
 * @since 1.0 22:30
 */
public interface RecordModifierEntity extends Entity {
    String updateById = "updateById";
    String updateTime = "updateTime";

    String getUpdateById();

    void setUpdateById(String updateById);

    default void setUpdateByName(String updateByName) {

    }

    LocalDateTime getUpdateTime();

    void setUpdateTime(LocalDateTime updateTime);

    default void setUpdateTimeNow() {
        setUpdateTime(LocalDateTime.now());
    }

    @JsonIgnore
    default String getUpdateByIdProperty() {
        return updateById;
    }
}
