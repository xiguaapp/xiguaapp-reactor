package com.cn.xiguaapp.thrid.auth.constants.scope.impl;

import com.cn.xiguaapp.thrid.auth.constants.scope.AuthScope;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author xiguaapp
 * @desc 京东平台 OAuth 授权范围
 * @since 1.0 22:02
 */
@Getter
@AllArgsConstructor
public enum AuthJdScope implements AuthScope {
    /**
     * {@code scope} 含义，以{@code description} 为准
     */
    SNSAPI_BASE("snsapi_base", "基础授权", true);

    private String scope;
    private String description;
    private boolean isDefault;
}
