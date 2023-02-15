package com.cn.xiguaapp.r2dbc.common.core.web.reactive.path;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

/**
 * @author xiguaapp
 * @desc 构建新的请求地址前缀 如 /baidu/xxxx/xxxx
 *  格式："/baidu"
 * @since 1.0 21:01
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BuildPath implements BuildPathApi {
    private String path;

    @Override
    public String getPath() {
        return path;
    }
}
