/*
 *
 *       Copyright (C) <2018-2028>  <@author: xiguaapp @date: 2020/12/9 上午11:17 >
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

import com.cn.xiguaapp.common.gateway.bean.ServiceGrayConfig;
import com.google.common.collect.Maps;

import java.util.Map;

/**
 * @author xiguaapp
 */
public class DefaultEnvGrayManager implements EnvGrayManager {

    /**
     * key：serviceId，服务对应的灰度配置
     */
    private final Map<String, ServiceGrayConfig> serviceGrayConfigMap = Maps.newConcurrentMap();

    /**
     * key:instanceId value:serviceId
     */
    private final Map<String, String> instanceIdServiceIdMap = Maps.newConcurrentMap();

    @Override
    public void saveServiceGrayConfig(ServiceGrayConfig serviceGrayConfig) {
        serviceGrayConfigMap.put(serviceGrayConfig.getServiceId(), serviceGrayConfig);
    }

    @Override
    public boolean containsKey(String serviceId, Object userKey) {
        if (serviceId == null || userKey == null) {
            return false;
        }
        ServiceGrayConfig grayConfig = this.getGrayConfig(serviceId);
        return grayConfig != null && grayConfig.containsKey(userKey);
    }

    @Override
    public String getVersion(String serviceId, String nameVersion) {
        if (serviceId == null || nameVersion == null) {
            return null;
        }
        boolean opened = instanceIdServiceIdMap.containsValue(serviceId);
        // 没有开启灰度
        if (!opened) {
            return null;
        }
        ServiceGrayConfig grayConfig = this.getGrayConfig(serviceId);
        return grayConfig != null ? grayConfig.getVersion(nameVersion) : null;
    }

    private ServiceGrayConfig getGrayConfig(String serviceId) {
        if (serviceId == null || !instanceIdServiceIdMap.containsValue(serviceId)) {
            return null;
        }
        return serviceGrayConfigMap.get(serviceId);
    }

    @Override
    public void openGray(String instanceId, String serviceId) {
        instanceIdServiceIdMap.putIfAbsent(instanceId, serviceId);
    }

    @Override
    public void closeGray(String instanceId) {
        instanceIdServiceIdMap.remove(instanceId);
    }

    @Override
    public void load() {

    }
}
