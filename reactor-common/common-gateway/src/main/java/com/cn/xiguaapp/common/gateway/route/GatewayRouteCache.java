/*
 *
 *       Copyright (C) <2018-2028>  <@author: xiguaapp @date: 2020/12/9 上午11:02 >
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

import com.alibaba.fastjson.JSON;
import com.cn.xiguaapp.common.gateway.bean.RouteDefinition;
import com.cn.xiguaapp.common.gateway.bean.ServiceDefinition;
import com.cn.xiguaapp.common.gateway.bean.ServiceRouteInfo;
import com.cn.xiguaapp.common.gateway.manage.RouteLoader;
import com.cn.xiguaapp.common.gateway.manage.RouteRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * @author xiguaapp
 */
@Slf4j
public class GatewayRouteCache implements RouteLoader {

    /**
     * KEY:serviceId, value: md5
     */
    private Map<String, String> serviceIdMd5Map = new HashMap<>();

    private RouteRepository<GatewayTargetRoute> routeRepository;

    public GatewayRouteCache(RouteRepository<GatewayTargetRoute> routeRepository) {
        this.routeRepository = routeRepository;
    }

    @Override
    public void load(ServiceRouteInfo serviceRouteInfo, Consumer<Object> callback) {
        try {
            String serviceId = serviceRouteInfo.getServiceId();
            String newMd5 = buildMd5(serviceRouteInfo.getRouteDefinitionList());
            String oldMd5 = serviceIdMd5Map.get(serviceId);
            if (Objects.equals(newMd5, oldMd5)) {
                return;
            }
            serviceIdMd5Map.put(serviceId, newMd5);

            List<RouteDefinition> routeDefinitionList = serviceRouteInfo.getRouteDefinitionList();
            for (RouteDefinition routeDefinition : routeDefinitionList) {
                this.add(serviceId, routeDefinition);
                if (log.isDebugEnabled()) {
                    log.debug("新增路由：{}", JSON.toJSONString(routeDefinition));
                }
            }
            this.refresh();
            callback.accept(null);
        } catch (Exception e) {
            log.error("加载路由信息失败，serviceRouteInfo:{}", serviceRouteInfo, e);
        }
    }

    /**
     * 添加路由信息到本地缓存，这里添加后Spring Cloud Gateway并不会识别路由，需要调用refresh()方法
     *
     * @see #refresh()
     * @param serviceId 服务id
     * @param routeDefinition 路由信息
     */
    public void add(String serviceId, RouteDefinition routeDefinition) {
        GatewayTargetRoute targetRoute = new GatewayTargetRoute(new ServiceDefinition(serviceId), routeDefinition);
        routeRepository.add(targetRoute);
    }

    /**
     * 刷新路由到Spring Cloud Gateway路由管理器当中
     */
    public void refresh() {
        this.routeRepository.refresh();
    }

    /**
     * 构建路由id MD5
     *
     * @param routeDefinitionList 路由列表
     * @return 返回MD5
     */
    private String buildMd5(List<RouteDefinition> routeDefinitionList) {
        List<String> routeIdList = routeDefinitionList.stream()
                .map(JSON::toJSONString)
                .sorted()
                .collect(Collectors.toList());
        String md5Source = org.apache.commons.lang3.StringUtils.join(routeIdList, "");
        return DigestUtils.md5DigestAsHex(md5Source.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public void remove(String serviceId) {
        serviceIdMd5Map.remove(serviceId);
        routeRepository.deleteAll(serviceId);
    }

}
