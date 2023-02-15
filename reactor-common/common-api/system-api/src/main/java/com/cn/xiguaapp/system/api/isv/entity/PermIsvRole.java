package com.cn.xiguaapp.system.api.isv.entity;

import com.cn.xiguaapp.r2dbc.common.api.crud.entity.GenericEntity;
import com.cn.xiguaapp.r2dbc.common.core.generator.Generators;
import com.cn.xiguaapp.r2dbc.orm.rdb.mapping.annotation.Comment;
import io.swagger.annotations.ApiModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Table;

/**
 * @author xiguaapp
 * @project-name xiguaapp-reactor
 * @Date 2020/10/16
 * @desc
 */
@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "perm_isv_role")
@Comment("isv密钥管理")
@ApiModel(value = "perm_isv_role",description = "isv密钥管理")
public class PermIsvRole extends GenericEntity<Long> {
    /** isv_info表id, 数据库字段：isv_id */
    @Schema(description = "isv_info表id")
    @Comment("isv_info表id")
    @Column(length = 100)
    private Long isvId;

    /** 角色code, 数据库字段：role_code */
    @Schema(description = "角色编码")
    @Column(length = 255)
    @Comment("角色编码")
    private String roleCode;
    @Override
    @GeneratedValue(generator = Generators.SNOW_FLAKE)
    public Long getId() {
        return super.getId();
    }

}
