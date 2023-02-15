/*
 *
 *       Copyright (C) <2018-2028>  <@author: xiguaapp @date: 2020/12/9 上午11:01 >
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

package com.cn.xiguaapp.common.gateway.manage;


import com.cn.xiguaapp.common.gateway.bean.ServiceRouteInfo;

import java.util.function.Consumer;

/**
 * @author xiguaapp
 */
public interface RouteLoader {
    /**
     * 加载路由
     *
     * @param serviceRouteInfo 服务路由信息
     * @param callback         加载成功后回调
     */
    void load(ServiceRouteInfo serviceRouteInfo, Consumer<Object> callback);

    /**
     * 移除某个微服务下的所有路由信息
     *
     * @param serviceId 服务id
     */
    void remove(String serviceId);
}
