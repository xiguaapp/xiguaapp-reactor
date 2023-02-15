/*
 *
 *       Copyright (C) <2018-2028>  <@author: xiguaapp @date: 2020/12/7 下午7:07 >
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

package com.cn.ykyoung.server.route;

import lombok.Data;

import java.util.List;

/**
 * @author xiguaapp
 */
@Data
public class RouteDefinition {
    /**
     * 路由的Id（接口名+版本号），确保此id全局唯一
     */
    private String id;

    /**
     * 接口名
     */
    private String name;

    /**
     * 版本号
     */
    private String version;

    /**
     * 路由断言集合配置
     */
    private List<GatewayPredicateDefinition> predicates;

    /**
     * 路由过滤器集合配置
     */
    private List<GatewayFilterDefinition> filters;

    /**
     * 路由规则转发的目标uri
     */
    private String uri;

    /**
     * uri后面跟的path
     */
    private String path;

    /**
     * 路由执行的顺序
     */
    private int order = 0;

    /**
     * 是否忽略验证，业务参数验证除外
     */
    private int ignoreValidate;

    /**
     * 状态，0：待审核，1：启用，2：禁用
     */
    private int status = 1;

    /**
     * 是否合并结果
     */
    private int mergeResult;

    /**
     * 是否需要授权才能访问
     */
    private int permission;

    /**
     * 是否需要token
     */
    private int needToken;

    /**
     * 是否是兼容模式，即使用了@ApiAbility注解
     */
    private int compatibleMode;
}