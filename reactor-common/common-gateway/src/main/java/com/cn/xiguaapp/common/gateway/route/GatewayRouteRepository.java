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

package com.cn.xiguaapp.common.gateway.route;

import com.cn.xiguaapp.common.gateway.bean.RouteDefinition;
import com.cn.xiguaapp.common.gateway.manage.RouteRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.ApplicationContext;
import reactor.core.publisher.Flux;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Collections.synchronizedMap;

/**
 * 路由存储管理，负责动态更新路由
 *
 * @author xiguaapp
 */
@Slf4j
public class GatewayRouteRepository implements RouteRepository<GatewayTargetRoute>, RouteLocator {

    private static final Map<String, GatewayTargetRoute> routes = synchronizedMap(new LinkedHashMap<>());

    @Autowired
    private RouteLocatorBuilder routeLocatorBuilder;

    @Autowired
    private ApplicationContext applicationContext;

    private volatile RouteLocator routeLocator;

    @Override
    public Flux<Route> getRoutes() {
        if (routeLocator == null) {
            return Flux.empty();
        }
        return routeLocator.getRoutes();
    }

    @Override
    public void refresh() {
        RouteLocatorBuilder.Builder builder = routeLocatorBuilder.routes();
        List<RouteDefinition> routeDefinitionList = routes.values()
                .stream()
                .map(GatewayTargetRoute::getRouteDefinition)
                .collect(Collectors.toList());
        routeDefinitionList.forEach(routeDefinition -> builder.route(routeDefinition.getId(),
                r -> r.path(routeDefinition.getPath())
                        .uri(routeDefinition.getUri())));
        this.routeLocator = builder.build();

        // 触发
        applicationContext.publishEvent(new RefreshRoutesEvent(new Object()));
    }

    /**
     * 根据ID获取路由
     */
    @Override
    public GatewayTargetRoute get(String id) {
        if (id == null) {
            return null;
        }
        return routes.get(id);
    }


    @Override
    public Collection<GatewayTargetRoute> getAll() {
        return routes.values();
    }

    /**
     * 增加路由
     */
    @Override
    public String add(GatewayTargetRoute targetRoute) {
        RouteDefinition routeDefinition = targetRoute.getRouteDefinition();
        routes.put(routeDefinition.getId(), targetRoute);
        return "success";
    }

    @Override
    public void update(GatewayTargetRoute targetRoute) {
        RouteDefinition baseRouteDefinition = targetRoute.getRouteDefinition();
        routes.put(baseRouteDefinition.getId(), targetRoute);
    }

    /**
     * 删除路由
     */
    @Override
    public void delete(String id) {
        routes.remove(id);
    }

    @Override
    public void deleteAll(String serviceId) {
        List<String> idList = routes.values().stream()
                .filter(zuulTargetRoute -> StringUtils.equalsIgnoreCase(serviceId, zuulTargetRoute.getServiceDefinition().getServiceId()))
                .map(zuulTargetRoute -> zuulTargetRoute.getRouteDefinition().getId())
                .collect(Collectors.toList());

        for (String id : idList) {
            this.delete(id);
        }
    }

}