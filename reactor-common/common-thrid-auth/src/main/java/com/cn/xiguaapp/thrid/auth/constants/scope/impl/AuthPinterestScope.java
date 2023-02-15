package com.cn.xiguaapp.thrid.auth.constants.scope.impl;

import com.cn.xiguaapp.thrid.auth.constants.scope.AuthScope;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author xiguaapp
 * @desc Pinterest 平台 OAuth 授权范围
 * @since 1.0 22:06
 */
@Getter
@AllArgsConstructor
public enum AuthPinterestScope implements AuthScope {

    /**
     * {@code scope} 含义，以{@code description} 为准
     */
    READ_PUBLIC("read_public", "Use GET method on a user’s Pins, boards.", true),
    WRITE_PUBLIC("write_public", "Use PATCH, POST and DELETE methods on a user’s Pins and boards.", false),
    READ_RELATIONSHIPS("read_relationships", "Use GET method on a user’s follows and followers (on boards, users and interests).", false),
    WRITE_RELATIONSHIPS("write_relationships", "Use PATCH, POST and DELETE methods on a user’s follows and followers (on boards, users and interests).", false),
    ;

    private String scope;
    private String description;
    private boolean isDefault;

}