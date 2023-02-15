/*
 *
 *       Copyright (C) <2018-2028>  <@author: xiguaapp @date: 2020/12/9 上午11:47 >
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

import com.cn.xiguaapp.common.gateway.bean.ChannelMsg;
import com.cn.xiguaapp.common.gateway.bean.InstanceDefinition;
import com.cn.xiguaapp.common.gateway.bean.ServiceGrayConfig;
import com.cn.xiguaapp.common.gateway.bean.ServiceGrayDefinition;
import com.cn.xiguaapp.common.gateway.loadbalance.RegistryEvent;
import com.cn.xiguaapp.common.gateway.manage.DefaultEnvGrayManager;
import com.cn.xiguaapp.system.api.gray.entity.ConfigGray;
import com.cn.xiguaapp.system.api.gray.service.IConfigGrayClient;
import com.cn.xiguaapp.system.api.gray.service.IConfigGrayInstanceClient;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import javax.annotation.Resource;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 存放用户key，这里放在本机内容，如果灰度发布保存的用户id数量偏多，可放在redis中
 *
 * @author xiguaapp
 */
@Slf4j
@Component
public class DbEnvGrayFunction extends DefaultEnvGrayManager implements RegistryEvent {

    private static final Integer STATUS_ENABLE = 1;

    private static final Function<String[], String> FUNCTION_KEY = arr -> arr[0];
    private static final Function<String[], String> FUNCTION_VALUE = arr -> arr[1];

    @Resource
    @Lazy
    private IConfigGrayClient configGrayMapper;

    @Resource
    @Lazy
    private IConfigGrayInstanceClient configGrayInstanceMapper;

    @Override
    public void onRegistry(InstanceDefinition instanceDefinition) {
        configGrayInstanceMapper
                .findByInstance(instanceDefinition.getInstanceId())
                .filter(Objects::nonNull)
                .filter(oo-> oo.getStatus().equals(STATUS_ENABLE))
                .subscribe(oo -> {
                    log.info("实例[{}]开启灰度发布", oo.getInstanceId());
                    this.openGray(oo.getInstanceId(), oo.getServiceId());
                });
    }

    @Override
    public Consumer<String> onRemove() {
        return serviceId->{};
    }

    @Override
    public void load() {
        configGrayMapper.findAll().flatMap(oo->{
            this.setServiceGrayConfig().accept(oo);
            return Mono.empty();
        }).publishOn(Schedulers.elastic());
        log.info("====加载结束====");
    }

    /**
     * 设置用户key
     *
     * @see ConfigGray 灰度配置
     */
    public Consumer<ConfigGray> setServiceGrayConfig() {
        return configGray -> {
            if (configGray == null) {
                return;
            }
            String userKeyData = configGray.getUserKeyContent();
            String nameVersionContent = configGray.getNameVersionContent();
            String[] userKeys = StringUtils.split(userKeyData, ',');
            String[] nameVersionList = StringUtils.split(nameVersionContent, ',');
            log.info("灰度配置，userKeys.length:{}, nameVersionList:{}", userKeys.length, Arrays.toString(nameVersionList));

            Set<String> userKeySet = Stream.of(userKeys)
                    .collect(Collectors.toCollection(Sets::newConcurrentHashSet));

            Map<String, String> grayNameVersionMap = Stream.of(nameVersionList)
                    .map(nameVersion -> StringUtils.split(nameVersion, '='))
                    .collect(Collectors.toConcurrentMap(FUNCTION_KEY, FUNCTION_VALUE));

            ServiceGrayConfig serviceGrayConfig = new ServiceGrayConfig();
            serviceGrayConfig.setServiceId(configGray.getServiceId());
            serviceGrayConfig.setUserKeys(userKeySet);
            serviceGrayConfig.setGrayNameVersion(grayNameVersionMap);
            this.saveServiceGrayConfig(serviceGrayConfig);
        };
    }

    @Override
    public Consumer<ChannelMsg> process() {
        return channelMsg -> {
            ServiceGrayDefinition userKeyDefinition = channelMsg.toObject(ServiceGrayDefinition.class);
            String serviceId = userKeyDefinition.getServiceId();
            switch (channelMsg.getOperation()) {
                case "set" -> configGrayMapper.findByServiceId(serviceId).filter(Objects::nonNull)
                        .doOnNext(pp->this.setServiceGrayConfig().accept(pp));
                case "open" -> openGray(userKeyDefinition.getInstanceId(), serviceId);
                case "close" -> closeGray(userKeyDefinition.getInstanceId());
                default -> log.error("渠道不存在");
            }
        };
    }


}
