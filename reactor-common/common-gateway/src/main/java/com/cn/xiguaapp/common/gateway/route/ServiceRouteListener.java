/*
 *
 *       Copyright (C) <2018-2028>  <@author: xiguaapp @date: 2020/12/9 上午11:54 >
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
import com.cn.xiguaapp.common.gateway.bean.InstanceDefinition;
import com.cn.xiguaapp.common.gateway.bean.ServiceRouteInfo;
import com.cn.xiguaapp.common.gateway.manage.RouteLoader;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

/**
 * @author xiguaapp
 */
@Slf4j
public class ServiceRouteListener extends BaseServiceListener {

    private static final String SOP_ROUTES_PATH = "/routes";

    private static final String METADATA_SOP_ROUTES_PATH = "routes.path";

//    @Autowired
//    private GatewayRouteCache gatewayRouteCache;
    @Autowired
    private RouteLoader gatewayRouteCache;

    @Autowired
    private RoutesProcessor routesProcessor;

    @Override
    public void onRemoveService(String serviceId) {
        log.info("服务下线，删除路由配置，serviceId: {}", serviceId);
        gatewayRouteCache.remove(serviceId);
        routesProcessor.removeAllRoutes(serviceId);
    }

    @Override
    public void onAddInstance(InstanceDefinition instance) {
        String serviceName = instance.getServiceId();
        String url = getRouteRequestUrl(instance);
        log.info("拉取路由配置，serviceId: {}, url: {}", serviceName, url);
        ResponseEntity<String> responseEntity = getRestTemplate().getForEntity(url, String.class);
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            String body = responseEntity.getBody();
            ServiceRouteInfo serviceRouteInfo = JSON.parseObject(body, ServiceRouteInfo.class);
            gatewayRouteCache.load(serviceRouteInfo, callback -> routesProcessor.saveRoutes(serviceRouteInfo, instance));
        } else {
            log.error("拉取路由配置异常，url: {}, status: {}, body: {}", url, responseEntity.getStatusCodeValue(), responseEntity.getBody());
        }
    }

    protected HttpEntity<String> getHttpEntity() {
        HttpHeaders headers = new HttpHeaders();
        return new HttpEntity<>(headers);
    }

    /**
     * 拉取路由请求url
     *
     * @param instance 服务实例
     * @return 返回最终url
     */
    private String getRouteRequestUrl(InstanceDefinition instance) {
        Map<String, String> metadata = instance.getMetadata();
        String customPath = metadata.get(METADATA_SOP_ROUTES_PATH);
        String homeUrl;
        String servletPath;
        // 如果metadata中指定了获取路由的url
        if (StringUtils.isNotBlank(customPath)) {
            // 自定义完整的url
            if (customPath.startsWith("http")) {
                homeUrl = customPath;
                servletPath = "";
            } else {
                homeUrl = getHomeUrl(instance);
                servletPath = customPath;
            }
        } else {
            // 默认处理
            homeUrl = getHomeUrl(instance);
            String contextPath = this.getContextPath(metadata);
            servletPath = contextPath + SOP_ROUTES_PATH;
        }
        if (StringUtils.isNotBlank(servletPath) && !servletPath.startsWith("/")) {
            servletPath = '/' + servletPath;
        }
        String query = buildQuery();
        return homeUrl + servletPath + query;
    }

    private static String getHomeUrl(InstanceDefinition instance) {
        return "http://" + instance.getIp() + ":" + instance.getPort();
    }
}
