/*
 *
 *       Copyright (C) <2018-2028>  <@author: xiguaapp @date: 2020/12/9 上午10:43 >
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

import com.cn.xiguaapp.common.gateway.bean.TargetRoute;

import java.util.Collection;

/**
 * @author xiguaapp
 */
public interface RouteRepository<T extends TargetRoute> {
    /**
     * 获取路由信息
     * @param id 路由id
     * @return 返回路由信息，找不到返回null
     */
    T get(String id);

    /**
     * 返回所有路由信息
     * @return 返回所有路由信息
     */
    Collection<T> getAll();

    /**
     * 添加路由
     * @param targetRoute 模板路由对象
     * @return 返回路由id
     */
    String add(T targetRoute);

    /**
     * 更新路由
     * @param targetRoute 模板路由对象
     */
    void update(T targetRoute);

    /**
     * 删除路由
     * @param id 路由id
     */
    void delete(String id);

    /**
     * 删除service下的所有路由
     * @param serviceId 服务id
     */
    void deleteAll(String serviceId);

    /**
     * 刷新
     */
    default void refresh() {}
}
