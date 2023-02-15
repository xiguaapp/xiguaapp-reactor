/*
 *
 *       Copyright (C) <2018-2028>  <@author: xiguaapp @date: 2020/12/7 下午5:58 >
 *
 *       Send: 1125698980@qq.com
 *
 *       This program is free software: you can redistribute it and/or modify
 *       it under the terms of the GNU General Public License as published by
 *       the Free Software Foundation, either version 3 of the License, or
 *       (at your option) any later version.
 *
 *       This program is distributed in the hope that it will be useful,
 *       but WITHOUT ANY WARRANTY; without even the implied warranty of
 *       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *       GNU General Public License for more details.
 *
 *       You should have received a copy of the GNU General Public License
 *       along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package com.cn.ykyoung.server.bean;

import com.cn.xiguapp.common.core.exception.ServiceResultBuilder;
import org.springframework.web.reactive.result.method.annotation.ApiArgumentResolver;
import com.cn.ykyoung.server.common.param.ReactorHandlerMethodArgumentResolver;
import com.cn.ykyoung.server.result.DefaultServiceResultBuilder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xiguaapp
 * @desc
 * @since 1.0 17:58
 */
@Data
public class ServiceConfig {
    private static ServiceConfig instance = new ServiceConfig();

    private ServiceConfig() {
    }

    public static ServiceConfig getInstance() {
        return instance;
    }

    /**
     * 返回结果处理
     */
    private ServiceResultBuilder serviceResultBuilder = new DefaultServiceResultBuilder();


    /**
     * 解析业务参数
     */
    private ReactorHandlerMethodArgumentResolver methodArgumentResolver = new ApiArgumentResolver();

    /**
     * 默认版本号
     */
    private String defaultVersion = "";

    /**
     * 错误模块
     */
    private List<String> i18nModules = new ArrayList<>();

}
