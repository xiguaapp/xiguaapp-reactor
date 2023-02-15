/*
 *
 *       Copyright (C) <2018-2028>  <@author: xiguaapp @date: 2020/12/9 上午11:24 >
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

package com.cn.xiguaapp.common.gateway.loadbalance;

import com.cn.xiguaapp.common.gateway.utils.SpringContext;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.Server;
import org.springframework.cloud.netflix.ribbon.DefaultServerIntrospector;
import org.springframework.cloud.netflix.ribbon.ServerIntrospector;
import org.springframework.cloud.netflix.ribbon.SpringClientFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.cn.xiguaapp.common.gateway.bean.GatewayConstants.*;

/**
 * 预发布、灰度发布服务器选择
 *
 * @author xiguaapp
 */
public abstract class LoadBalanceServerChooser<T, R> implements ServerChooserContext<T> {

    private SpringClientFactory clientFactory;

    /**
     * 选择服务器
     *
     * @param serviceId             serviceId，仅gateway网关有作用
     * @param exchange              请求上下文
     * @param loadBalancer          loadBalancer
     * @param superChooser          父类默认的选择
     * @param serverChooserFunction 执行选择操作
     * @return 返回服务器实例，没有选到则返回null
     */
    public R choose(
            String serviceId
            , T exchange
            , ILoadBalancer loadBalancer
            , Supplier<R> superChooser
            , Function<List<Server>, R> serverChooserFunction) {
        // 获取所有服务实例
        List<Server> servers = loadBalancer.getReachableServers();

        // 存放预发服务器
        List<Server> preServers = new ArrayList<>(4);
        // 存放灰度发布服务器
        List<Server> grayServers = new ArrayList<>(4);
        // 存放非预发服务器
        List<Server> notPreServers = new ArrayList<>(4);

        for (Server server : servers) {
            // 获取实例metadata
            Map<String, String> metadata = getMetadata(serviceId, server);
            // 是否开启了预发模式
            if (this.isPreServer(metadata)) {
                preServers.add(server);
            } else if (this.isGrayServer(metadata)) {
                grayServers.add(server);
            } else {
                notPreServers.add(server);
            }
        }
        notPreServers.addAll(grayServers);
        // 如果没有开启预发布服务和灰度发布，直接用默认的方式
        if (preServers.isEmpty() && grayServers.isEmpty()) {
            return superChooser.get();
        }
        // 如果是从预发布域名访问过来，则认为是预发布请求，选出预发服务器
        if (this.isRequestFromPreDomain(exchange)) {
            return serverChooserFunction.apply(preServers);
        }
        // 如果是灰度请求，则认为是灰度用户，选出灰度服务器
        if (this.isRequestGrayServer(exchange)) {
            return serverChooserFunction.apply(grayServers);
        }

        // 到这里说明不能访问预发/灰度服务器，则需要路由到非预发服务器
        // 注意：这里允许走灰度服务器，如果不允许走，注释notPreServers.addAll(grayServers);这行
        return serverChooserFunction.apply(notPreServers);
    }

    protected Map<String, String> getMetadata(String serviceId, Server server) {
        return serverIntrospector(serviceId).getMetadata(server);
    }

    protected SpringClientFactory getSpringClientFactory() {
        if (clientFactory == null) {
            clientFactory = SpringContext.getBean(SpringClientFactory.class);
        }
        return clientFactory;
    }

    public void setClientFactory(SpringClientFactory clientFactory) {
        this.clientFactory = clientFactory;
    }

    private ServerIntrospector serverIntrospector(String serviceId) {
        ServerIntrospector serverIntrospector = getSpringClientFactory().getInstance(serviceId,
                ServerIntrospector.class);
        if (serverIntrospector == null) {
            serverIntrospector = new DefaultServerIntrospector();
        }
        return serverIntrospector;
    }

    /**
     * 是否是预发布服务器
     *
     * @param metadata metadata
     * @return true：是
     */
    private boolean isPreServer(Map<String, String> metadata) {
        return Objects.equals(metadata.get(METADATA_ENV_KEY), METADATA_ENV_PRE_VALUE);
    }

    /**
     * 是否是灰度发布服务器
     *
     * @param metadata metadata
     * @return true：是
     */
    private boolean isGrayServer(Map<String, String> metadata) {
        return Objects.equals(metadata.get(METADATA_ENV_KEY), METADATA_ENV_GRAY_VALUE);
    }

}
