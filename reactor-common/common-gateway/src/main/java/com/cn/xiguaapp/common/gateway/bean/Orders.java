/*
 *
 *       Copyright (C) <2018-2028>  <@author: xiguaapp @date: 2020/12/9 上午11:07 >
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

package com.cn.xiguaapp.common.gateway.bean;

import org.springframework.core.Ordered;

/**
 * @author xiguaapp
 */
public class Orders {
    /** 验证拦截器order */
    public static final int VALIDATE_FILTER_ORDER = Ordered.HIGHEST_PRECEDENCE + 1000;

    /** 参数格式化过滤器 */
    public static final int PARAMETER_FORMATTER_FILTER_ORDER = VALIDATE_FILTER_ORDER + 1;

    /** 权限验证过滤 */
    public static final int PRE_ROUTE_PERMISSION_FILTER_ORDER = PARAMETER_FORMATTER_FILTER_ORDER + 100;

    /** 验证拦截器order */
    public static final int LIMIT_FILTER_ORDER = PRE_ROUTE_PERMISSION_FILTER_ORDER + 100;

    /** 灰度发布过滤器 */
    public static final int ENV_GRAY_FILTER_ORDER = LIMIT_FILTER_ORDER + 100;

}
