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
@Table(name = "isv_keys")
@ApiModel(value = "IsvKeys",description = "isv密钥管理")
@Comment("isv密钥管理")
public class IsvKeys extends GenericEntity<Long> {
    @Schema(description = "appkey")
    @Column(length = 200)
    @Comment("appkey")
    private String appKey;

    @Schema(description = "签名类型 1:RSA2,2:MD5")
    @Column(length = 2)
    @Comment("签名类型 1:RSA2,2:MD5")
    private Integer signType;

    @Schema(description = "secret signType=2时使用")
    @Comment("secret signType=2时使用")
    @Column(length = 255)
    private String secret;

    @Schema(description = "密钥格式 1：PKCS8(JAVA适用)，2：PKCS1(非JAVA适用)")
    @Comment("密钥格式 1：PKCS8(JAVA适用)，2：PKCS1(非JAVA适用)")
    @Column(length = 2)
    private Integer keyFormat;

    @Schema(description = "开发者生成的公钥")
    @Comment("开发者生成的公钥")
    @Column(length = 255)
    private String publicKeyIsv;

    @Schema(description = "开发者生成的私钥（交给开发者）")
    @Comment("开发者生成的私钥（交给开发者）")
    @Column(length = 255)
    private String privateKeyIsv;

    @Schema(description = "平台生成的公钥（交给开发者）")
    @Column(length = 255)
    @Comment("平台生成的公钥（交给开发者）")
    private String publicKeyPlatform;

    @Schema(description = "平台生成的私钥")
    @Column(length = 255)
    @Comment("平台生成的私钥")
    private String privateKeyPlatform;
    @Override
    @GeneratedValue(generator = Generators.SNOW_FLAKE)
    public Long getId() {
        return super.getId();
    }
}
