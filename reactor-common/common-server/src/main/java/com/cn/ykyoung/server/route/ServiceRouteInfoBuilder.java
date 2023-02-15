/*
 *
 *       Copyright (C) <2018-2028>  <@author: xiguaapp @date: 2020/12/7 下午7:08 >
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

import com.cn.ykyoung.server.bean.ServiceApiInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.core.env.Environment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author xiguaapp
 */
@Slf4j
public class ServiceRouteInfoBuilder {

    /**
     * 网关对应的LoadBalance协议
     */
    private static final String PROTOCOL_LOAD_BALANCE = "lb://";

    private static final String PATH_SPLIT = "/";

    private static final String DEFAULT_CONTEXT_PATH = "/";

    private final Environment environment;

    public ServiceRouteInfoBuilder(Environment environment) {
        this.environment = environment;
    }

    public ServiceRouteInfo build(ServiceApiInfo serviceApiInfo) {
        return this.buildServiceGatewayInfo(serviceApiInfo);
    }

    /**
     * 构建接口信息，符合spring cloud gateway的格式
     *
     * @param serviceApiInfo 服务接口信息
     * @return 返回服务路由信息
     */
    protected ServiceRouteInfo buildServiceGatewayInfo(ServiceApiInfo serviceApiInfo) {
        List<ServiceApiInfo.ApiMeta> apis = serviceApiInfo.getApis();
        List<RouteDefinition> routeDefinitionList = new ArrayList<>(apis.size());
        for (ServiceApiInfo.ApiMeta apiMeta : apis) {
            RouteDefinition routeDefinition = this.buildGatewayRouteDefinition(serviceApiInfo, apiMeta);
            routeDefinitionList.add(routeDefinition);
        }
        ServiceRouteInfo serviceRouteInfo = new ServiceRouteInfo();
        serviceRouteInfo.setServiceId(serviceApiInfo.getServiceId());
        serviceRouteInfo.setRouteDefinitionList(routeDefinitionList);
        return serviceRouteInfo;
    }


    protected RouteDefinition buildGatewayRouteDefinition(ServiceApiInfo serviceApiInfo, ServiceApiInfo.ApiMeta apiMeta) {
        RouteDefinition routeDefinition = new RouteDefinition();
        // 唯一id规则：接口名 + 版本号
        String routeId = apiMeta.fetchNameVersion();
        BeanUtils.copyProperties(apiMeta, routeDefinition);
        routeDefinition.setId(routeId);
        routeDefinition.setFilters(Collections.emptyList());
        routeDefinition.setPredicates(Collections.emptyList());
        String uri = this.buildUri(serviceApiInfo, apiMeta);
        String path = this.buildServletPath(serviceApiInfo, apiMeta);
        routeDefinition.setUri(uri);
        routeDefinition.setPath(path);
        return routeDefinition;
    }

    protected String buildUri(ServiceApiInfo serviceApiInfo, ServiceApiInfo.ApiMeta apiMeta) {
        return PROTOCOL_LOAD_BALANCE + serviceApiInfo.getServiceId();
    }

    protected String buildServletPath(ServiceApiInfo serviceApiInfo, ServiceApiInfo.ApiMeta apiMeta) {
        String contextPath = getContextPathCompatibility();
        String servletPath = apiMeta.getPath();
        if (servletPath == null) {
            servletPath = "";
        }
        if (!servletPath.startsWith(PATH_SPLIT)) {
            servletPath = PATH_SPLIT + servletPath;
        }
        if (DEFAULT_CONTEXT_PATH.equals(contextPath)) {
            return servletPath;
        } else {
            return contextPath + servletPath;
        }
    }

    /**
     * 获取context-path，兼容老版本
     * @return 返回context-path，没有返回"/"
     */
    private String getContextPathCompatibility() {
        return environment.getProperty("server.servlet.context-path",
                environment.getProperty("server.context-path", DEFAULT_CONTEXT_PATH)
        );
    }

    private void checkPath(String path, String errorMsg) {
        if (path.contains(PATH_SPLIT)) {
            throw new IllegalArgumentException(errorMsg);
        }
    }

    public Environment getEnvironment() {
        return environment;
    }
}
