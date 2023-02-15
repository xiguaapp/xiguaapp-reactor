/*
 *
 *       Copyright (C) <2018-2028>  <@author: xiguaapp @date: 2020/12/9 上午11:44 >
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

package com.cn.xiguaapp.common.gateway.nacos.config;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.alibaba.nacos.api.naming.pojo.ListView;
import com.cn.xiguaapp.common.gateway.bean.InstanceDefinition;
import com.cn.xiguaapp.common.gateway.loadbalance.RegistryEvent;
import com.cn.xiguaapp.common.gateway.route.BaseRegistryListener;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEvent;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static com.cn.xiguaapp.common.gateway.bean.GatewayConstants.METADATA_KEY_TIME_STARTUP;

/**
 * 加载服务路由，nacos实现
 *
 * @author xiguaapp
 */
@Slf4j
public class NacosRegistryListener extends BaseRegistryListener {

    private volatile Set<NacosServiceHolder> cacheServices = new HashSet<>();

    @Value("${nacos.discovery.group:${spring.cloud.nacos.discovery.group:DEFAULT_GROUP}}")
    private String nacosGroup;

    @Autowired
    private NacosDiscoveryProperties nacosDiscoveryProperties;

    @Autowired
    private List<RegistryEvent> registryEventList;

    @Override
    public synchronized void onEvent(ApplicationEvent applicationEvent) {
        List<NacosServiceHolder> serviceList = this.getServiceList();
        final Set<NacosServiceHolder> currentServices = new HashSet<>(serviceList);

        // 删除现有的，剩下的就是新服务
        currentServices.removeAll(cacheServices);
        // 如果有新的服务注册进来
        if (currentServices.size() > 0) {
            currentServices.forEach(nacosServiceHolder -> {
                Instance instance = nacosServiceHolder.getInstance();
                InstanceDefinition instanceDefinition = new InstanceDefinition();
                instanceDefinition.setInstanceId(instance.getInstanceId());
                instanceDefinition.setServiceId(nacosServiceHolder.getServiceId());
                instanceDefinition.setIp(instance.getIp());
                instanceDefinition.setPort(instance.getPort());
                instanceDefinition.setMetadata(instance.getMetadata());
                pullRoutes(instanceDefinition);
                if (registryEventList != null) {
                    registryEventList.forEach(registryEvent -> registryEvent.onRegistry(instanceDefinition));
                }
            });
        }

        // 如果有服务下线
        Set<String> removedServiceIdList = getRemovedServiceId(serviceList);
        // 移除
        this.doRemove(removedServiceIdList);

        cacheServices = new HashSet<>(serviceList);
    }

    /**
     * 获取建康的服务实例
     *
     * @return 没有返回空的list
     */
    private List<NacosServiceHolder> getServiceList() {
        NamingService namingService = nacosDiscoveryProperties.namingServiceInstance();
        ListView<String> servicesOfServer = null;
        try {
            servicesOfServer = namingService.getServicesOfServer(1, Integer.MAX_VALUE);
        } catch (NacosException e) {
            log.error("namingService.getServicesOfServer()错误", e);
        }
        if (servicesOfServer == null || CollectionUtils.isEmpty(servicesOfServer.getData())) {
            return Collections.emptyList();
        }
        return servicesOfServer
                .getData()
                .stream()
                .map(serviceName -> {
                    List<Instance> allInstances;
                    try {
                        // 获取服务实例
                       allInstances = namingService.getAllInstances(serviceName, nacosGroup);
                    } catch (NacosException e) {
                        log.error("namingService.getAllInstances(serviceName)错误，serviceName：{}", serviceName, e);
                        return null;
                    }
                    if (CollectionUtils.isEmpty(allInstances)) {
                        return null;
                    }
                    return allInstances.stream()
                            // 只获取建康实例
                            .filter(Instance::isHealthy)
                            .map(instance -> {
                                String startupTime = instance.getMetadata().get(METADATA_KEY_TIME_STARTUP);
                                if (startupTime == null) {
                                    return null;
                                }
                                long time = NumberUtils.toLong(startupTime, 0);
                                return new NacosServiceHolder(serviceName, time, instance);
                            })
                            .filter(Objects::nonNull)
                            .max(Comparator.comparing(ServiceHolder::getLastUpdatedTimestamp))
                            .orElse(null);

                })
                .filter(Objects::nonNull)
                .filter(this::canOperator)
                .collect(Collectors.toList());
    }

    /**
     * 获取已经下线的serviceId
     *
     * @param serviceList 最新的serviceId集合
     * @return 返回已下线的serviceId
     */
    private Set<String> getRemovedServiceId(List<NacosServiceHolder> serviceList) {
        Set<String> cache = cacheServices.stream()
                .map(NacosServiceHolder::getServiceId)
                .collect(Collectors.toSet());

        Set<String> newList = serviceList.stream()
                .map(NacosServiceHolder::getServiceId)
                .collect(Collectors.toSet());

        cache.removeAll(newList);
        return cache;
    }

    private void doRemove(Set<String> deletedServices) {
        if (deletedServices == null) {
            return;
        }
        deletedServices.forEach(serviceId -> {
            this.removeRoutes(serviceId);
            if (registryEventList != null) {
                registryEventList.forEach(registryEvent -> registryEvent.onRemove().accept(serviceId));
            }
        });
    }

}
