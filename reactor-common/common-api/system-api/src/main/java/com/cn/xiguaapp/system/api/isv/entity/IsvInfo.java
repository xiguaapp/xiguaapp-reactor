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
 * @Date 2020/9/30
 * @desc
 */
@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "isv_info")
@ApiModel(value = "IsvInfo",description = "isv信息表")
@Comment("isv信息表")
public class IsvInfo extends GenericEntity<Long> {
    @Comment("appkey")
    @Column(length = 120)
    @Schema(description = "appkey")
    private String appKey;

    @Comment("公钥")
    @Schema(description = "公钥")
    private String pubKey;

    @Schema(description = "私钥")
    @Comment("私钥")
    private String priKey;

    @Comment("secret")
    @Schema(description = "secret")
    private String secret;

    @Schema(description = "状态:1启用 2禁用")
    @Column(length = 2)
    @Comment("状态:1启用 2禁用")
    private Integer status;

    @Schema(description = "签名类型 1:RSA2,2:md5")
    @Comment("签名类型1:RSA2,2:md5")
    @Column(length = 2)
    private Integer signType;

    @Comment("备注")
    @Column(length = 255)
    @Schema(description = "备注")
    private String remark;
    @Override
    @GeneratedValue(generator = Generators.SNOW_FLAKE)
    public Long getId() {
        return super.getId();
    }
}
