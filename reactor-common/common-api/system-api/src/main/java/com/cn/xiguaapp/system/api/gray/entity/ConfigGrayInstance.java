package com.cn.xiguaapp.system.api.gray.entity;

import com.cn.xiguaapp.r2dbc.common.api.crud.entity.GenericEntity;
import com.cn.xiguaapp.r2dbc.common.core.generator.Generators;
import com.cn.xiguaapp.r2dbc.orm.rdb.mapping.annotation.Comment;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Table;
import java.time.LocalDateTime;


/**
 * 表名：config_gray_instance
 *
 * @author xiguaapp
 */
@Comment("灰度发布实例")
@Table(name = "config_gray_instance")
@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConfigGrayInstance extends GenericEntity<Long> {
    @Override
    @GeneratedValue(generator = Generators.SNOW_FLAKE)
    public Long getId() {
        return super.getId();
    }

    @Column(length = 255)
    @Schema(description = "instance_id")
    @Comment("instance_id")
    private String instanceId;

    @Column(length = 255)
    @Schema(description = "serviceId")
    @Comment("serviceId")
    private String serviceId;

    @Column(length = 255)
    @Schema(description = " 0：禁用，1：启用,")
    @Comment(" 0：禁用，1：启用,")
    private Integer status;

    @Column(length = 20)
    @Schema(description = "创建时间")
    @Comment("创建时间")
    private LocalDateTime createTime;

    @Column(length = 20)
    @Schema(description = "更新时间")
    @Comment("更新时间")
    private LocalDateTime updateTime;
}
