/*
 *
 *       Copyright (C) <2018-2028>  <@author: xiguaapp @date: 2020/12/8 下午5:02 >
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

import com.cn.xiguaapp.common.gateway.bean.InstanceDefinition;
import com.cn.xiguaapp.common.gateway.manage.EnvironmentKeys;
import com.cn.xiguaapp.common.gateway.nacos.config.ServiceHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author xiguaapp
 */
@Slf4j
public abstract class BaseRegistryListener implements RegistryListener {

    private static final int FIVE_SECONDS = 1000 * 5;

    private final Map<String, Long> updateTimeMap = new ConcurrentHashMap<>(16);

    public static List<String> EXCLUDE_SERVICE_ID_LIST = new ArrayList<>(8);


    @Autowired
    private ServiceListener serviceListener;

    /**
     * 移除路由信息
     *
     * @param serviceId serviceId
     */
    public synchronized void removeRoutes(String serviceId) {
        serviceListener.onRemoveService(serviceId.toLowerCase());
    }

    /**
     * 拉取路由信息
     *
     * @param instance 服务实例
     */
    public synchronized void pullRoutes(InstanceDefinition instance) {
        // serviceId统一小写
        instance.setServiceId(instance.getServiceId().toLowerCase());
        serviceListener.onAddInstance(instance);
    }

    protected boolean canOperator(ServiceHolder serviceHolder) {
        String serviceId = serviceHolder.getServiceId();
        // 被排除的服务，不能操作
        if (isExcludeService(serviceId)) {
            return false;
        }
        // nacos会不停的触发事件，这里做了一层拦截
        // 同一个serviceId5秒内允许访问一次
        Long lastUpdateTime = updateTimeMap.getOrDefault(serviceId, 0L);
        long now = System.currentTimeMillis();
        boolean can = now - lastUpdateTime > FIVE_SECONDS;
        if (can) {
            updateTimeMap.put(serviceId, now);
        }
        return can;
    }

    /**
     * 是否是被排除的服务
     *
     * @param serviceId 服务id
     * @return true，是
     */
    private boolean isExcludeService(String serviceId) {
        List<String> excludeServiceIdList = getExcludeServiceId();
        for (String excludeServiceId : excludeServiceIdList) {
            if (excludeServiceId.equalsIgnoreCase(serviceId)) {
                return true;
            }
        }
        // 匹配正则
        String sopServiceExcludeRegex = EnvironmentKeys.SOP_SERVICE_EXCLUDE_REGEX.getValue();
        if (StringUtils.isNotBlank(sopServiceExcludeRegex)) {
            String[] regexArr = sopServiceExcludeRegex.split(";");
            for (String regex : regexArr) {
                if (serviceId.matches(regex)) {
                    return true;
                }
            }
        }
        return false;
    }

    private List<String> getExcludeServiceId() {
        String excludeServiceIds = EnvironmentKeys.SOP_SERVICE_EXCLUDE.getValue();
        List<String> excludeServiceIdList = new ArrayList<>(8);
        if (StringUtils.isNotBlank(excludeServiceIds)) {
            String[] serviceIdArr = excludeServiceIds.split(",");
            excludeServiceIdList.addAll(Arrays.asList(serviceIdArr));
        }
        excludeServiceIdList.addAll(EXCLUDE_SERVICE_ID_LIST);
        return excludeServiceIdList;
    }


}
