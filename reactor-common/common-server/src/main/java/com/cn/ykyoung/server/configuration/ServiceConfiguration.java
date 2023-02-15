/*
 *
 *       Copyright (C) <2018-2028>  <@author: xiguaapp @date: 2020/12/7 下午6:02 >
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

package com.cn.ykyoung.server.configuration;
import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.cloud.nacos.NacosServiceManager;
import com.alibaba.cloud.nacos.discovery.NacosWatch;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.Map;

/**
 * @author xiguaapp
 * @desc
 * @since 1.0 18:02
 */
@Slf4j
public class ServiceConfiguration extends SpringWebFluxConfiguration {
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty("spring.cloud.nacos.discovery.server-addr")
    public NacosWatch nacosWatch(NacosDiscoveryProperties nacosDiscoveryProperties, ObjectProvider<ThreadPoolTaskScheduler> taskScheduler, Environment environment) {
        Map<String, String> metadata = nacosDiscoveryProperties.getMetadata();
        String contextPath = environment.getProperty(METADATA_SERVER_CONTEXT_PATH);
        // 将context-path信息加入到metadata中
        if (contextPath != null) {
            metadata.put(METADATA_SERVER_CONTEXT_PATH, contextPath);
        }
        NacosServiceManager nacosServiceManager = new NacosServiceManager();
        // 在元数据中新增启动时间，不能修改这个值，不然网关拉取接口会有问题
        // 如果没有这个值，网关会忽略这个服务
        metadata.put("server.startup-time", String.valueOf(System.currentTimeMillis()));
        return new NacosWatch(nacosServiceManager,nacosDiscoveryProperties, taskScheduler);
    }
}
