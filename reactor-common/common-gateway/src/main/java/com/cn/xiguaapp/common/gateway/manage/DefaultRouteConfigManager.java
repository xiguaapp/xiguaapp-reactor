/*
 *
 *       Copyright (C) <2018-2028>  <@author: xiguaapp @date: 2020/12/9 上午10:39 >
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


import com.cn.xiguapp.common.core.utils.BeansUtils;
import com.cn.xiguaapp.common.gateway.bean.RouteConfig;
import com.cn.xiguaapp.common.gateway.bean.RouteStatus;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author xiguaapp
 */
public class DefaultRouteConfigManager implements RouteConfigManager {
    /**
     * key: routeId
     */
    protected static Map<String, RouteConfig> routeConfigMap = new ConcurrentHashMap<>(64);

    private static final RouteConfig DEFAULT_ROUTE_CONFIG;

    static {
        DEFAULT_ROUTE_CONFIG = new RouteConfig();
        DEFAULT_ROUTE_CONFIG.setStatus(RouteStatus.ENABLE.getStatus());
    }

    @Override
    public void load(String serviceId) {

    }

    @Override
    public void update(RouteConfig routeConfig) {
        this.doUpdate(routeConfig.getRouteId(), routeConfig);
    }

    protected void save(RouteConfig routeConfig) {
        Byte status = routeConfig.getStatus();
        if (status == null) {
            routeConfig.setStatus(RouteStatus.ENABLE.getStatus());
        }
        routeConfigMap.put(routeConfig.getRouteId(), routeConfig);
    }

    protected void doUpdate(String routeId, Object res) {
        RouteConfig routeConfig = routeConfigMap.get(routeId);
        if (routeConfig == null) {
            routeConfig = newRouteConfig();
            routeConfig.setRouteId(routeId);
            routeConfigMap.put(routeId, routeConfig);
        }
        BeansUtils.copyPropertiesIgnoreNull(res, routeConfig);
    }

    protected RouteConfig newRouteConfig() {
        return new RouteConfig();
    }

    @Override
    public RouteConfig get(String routeId) {
        return routeConfigMap.getOrDefault(routeId, DEFAULT_ROUTE_CONFIG);
    }
}
