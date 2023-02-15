/*
 *
 *       Copyright (C) <2018-2028>  <@author: xiguaapp @date: 2020/12/9 上午11:55 >
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

package com.cn.xiguaapp.gateway.server.function;

import com.cn.xiguapp.common.core.utils.BeansUtils;
import com.cn.xiguaapp.common.gateway.bean.InstanceDefinition;
import com.cn.xiguaapp.common.gateway.bean.ServiceRouteInfo;
import com.cn.xiguaapp.common.gateway.manage.ServiceBeanInitializer;
import com.cn.xiguaapp.common.gateway.route.RoutesProcessor;
import com.cn.xiguaapp.system.api.gray.param.ServiceRouteInfoAndInstance;
import com.cn.xiguaapp.system.api.route.service.IConfigServiceRouteClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * @author xiguaapp
 */
@Slf4j
@Component
public class DbRoutesProcessorFunction implements RoutesProcessor {

    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    @Lazy
    private IConfigServiceRouteClient configServiceRouteClient;

    @Override
    public void removeAllRoutes(String serviceId) {
        // 删除serviceId下所有的路由
        configServiceRouteClient.deleteByServiceId(serviceId);
    }

    @Override
    public synchronized void saveRoutes(ServiceRouteInfo serviceRouteInfo, InstanceDefinition instance) {
        log.info("保存路由信息到数据库，instance: {}", instance);
        configServiceRouteClient.saveRoutes(ServiceRouteInfoAndInstance
                .builder()
                .info(Mono.just(BeansUtils.copy(serviceRouteInfo,new com.cn.ykyoung.server.route.ServiceRouteInfo())))
                .instanceDefinition(Mono.just(BeansUtils.copy(instance, new com.cn.xiguaapp.system.api.route.entity.param.InstanceDefinition())))
                .build());
        String serviceId = serviceRouteInfo.getServiceId();
//      后续处理操作
        this.initServiceBeanInitializer(serviceId);
    }

    private void initServiceBeanInitializer(String serviceId) {
        Map<String, ServiceBeanInitializer> serviceBeanInitializerMap = applicationContext.getBeansOfType(ServiceBeanInitializer.class);
        serviceBeanInitializerMap.values().forEach(serviceBeanInitializer -> serviceBeanInitializer.load(serviceId));
    }
}
