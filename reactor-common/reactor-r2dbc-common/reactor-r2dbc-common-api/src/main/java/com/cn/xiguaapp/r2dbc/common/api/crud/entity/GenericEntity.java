package com.cn.xiguaapp.r2dbc.common.api.crud.entity;

import com.cn.xiguapp.common.core.bean.ToString;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * @author xiguaapp
 * @desc 自动实体生成数据库及字段
 * @since 1.0 11:22
 */
@Getter
@Setter
public class GenericEntity <ID> implements Entity{
    /**
     * id 可通过重写getId()此方法重新定义id生成方式
     */
    @Column(length = 64,updatable = false)
    @Id
    @GeneratedValue(generator = "default_id")
    @Schema(description = "id")
    private ID id;
    public String toString(String... ignoreProperty) {
        return ToString.toString(this, ignoreProperty);
    }

    @Override
    public String toString() {
        return ToString.toString(this);
    }

}
