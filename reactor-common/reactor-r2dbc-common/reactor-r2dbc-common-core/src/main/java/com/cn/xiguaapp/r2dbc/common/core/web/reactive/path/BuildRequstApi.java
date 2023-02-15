package com.cn.xiguaapp.r2dbc.common.core.web.reactive.path;

/**
 * @author xiguaapp
 * @desc
 * @since 1.0 21:04
 */
public interface BuildRequstApi {
    default String getPathApi(){
        return buildPath().getPath();
    };
    /**
     * 构建请求地址
     * @see BuildPath
     * @return buildPath
     */
    BuildPath buildPath();
}
