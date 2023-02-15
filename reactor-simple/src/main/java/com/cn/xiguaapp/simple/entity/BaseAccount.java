package com.cn.xiguaapp.simple.entity;

import com.cn.xiguaapp.r2dbc.common.api.crud.entity.GenericEntity;
import com.cn.xiguaapp.r2dbc.common.core.generator.Generators;
import com.cn.xiguaapp.r2dbc.orm.rdb.mapping.annotation.Comment;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Table;

/**
 * 系统用户-登录账号
 *
 * @author xiguaapp
 */
@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "base_account")
@Comment("系统用户测试")
public class BaseAccount extends GenericEntity<Long> {

    /**
     * 系统用户Id
     */
    @Column(length = 64)
    @Comment("系统用户id")
    @Schema(description = "系统用户id")
    private Long userId;

    /**
     * 标识：手机号、邮箱、 系统用户名、或第三方应用的唯一标识
     */
    @Column(length = 64)
    @Comment("标识：手机号、邮箱、 系统用户名、或第三方应用的唯一标识")
    private String account;

    /**
     *
     */
    @Column(length = 64)
    @Comment("密码凭证：站内的保存密码、站外的不保存或保存token")
    private String password;

    /**
     * 登录类型:password-密码、mobile-手机号、email-邮箱、weixin-微信、weibo-微博、qq-等等
     */
    @Comment("登录类型:password-密码、mobile-手机号、email-邮箱、weixin-微信、weibo-微博、qq-等等")
    @Column(length = 64)
    private String accountType;

    /**
     * 注册IP
     */
    @Column(length = 64)
    @Comment("注册ip")
    private String registerIp;

    /**
     * 状态:0-禁用 1-启用 2-锁定
     */
    @Column(length = 64)
    @Comment("状态:0-禁用 1-启用 2-锁定")
    private Integer status;

    /**
     * 账号域
     */
    @Column(length = 64)
    @Comment("账号域")
    private String domain;

    @Override
    @GeneratedValue(generator = Generators.SNOW_FLAKE)
    public Long getId() {
        return super.getId();
    }
    
}
