/*
 *
 *       Copyright (C) <2018-2028>  <@author: xiguaapp @date: 2020/12/9 上午11:23 >
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

import com.cn.xiguaapp.common.gateway.bean.ApiParam;
import com.cn.xiguaapp.common.gateway.common.ServerWebExchangeUtil;
import com.cn.xiguaapp.common.gateway.utils.LoadBalanceUtil;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.Server;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.netflix.ribbon.DefaultServerIntrospector;
import org.springframework.cloud.netflix.ribbon.RibbonLoadBalancerClient;
import org.springframework.cloud.netflix.ribbon.RibbonUtils;
import org.springframework.cloud.netflix.ribbon.ServerIntrospector;
import org.springframework.cloud.netflix.ribbon.SpringClientFactory;
import org.springframework.web.server.ServerWebExchange;

import java.util.List;

/**
 * 重写负载均衡处理。
 * 默认使用的是RibbonLoadBalancerClient类，详见org.springframework.cloud.netflix.ribbon.RibbonAutoConfiguration#loadBalancerClient()
 *
 * @author xiguaapp
 */
public class SopLoadBalancerClient extends RibbonLoadBalancerClient implements ServerChooserContext<ServerWebExchange> {

    private final SpringClientFactory clientFactory;
    private GatewayLoadBalanceServerChooser loadBalanceServerChooser;

    public SopLoadBalancerClient(SpringClientFactory clientFactory) {
        super(clientFactory);
        this.clientFactory = clientFactory;
        this.loadBalanceServerChooser = new GatewayLoadBalanceServerChooser(clientFactory);
    }

    /**
     * New: Select a server using a 'key'.
     */
    @Override
    public ServiceInstance choose(String serviceId, Object hint) {
        return loadBalanceServerChooser.choose(
                serviceId
                , (ServerWebExchange) hint
                , this.getLoadBalancer(serviceId)
                , () -> super.choose(serviceId, hint)
                , (servers) -> getRibbonServer(serviceId, servers)
        );
    }

    @Override
    public ApiParam getApiParam(ServerWebExchange exchange) {
        return ServerWebExchangeUtil.getApiParam(exchange);
    }

    @Override
    public String getHost(ServerWebExchange exchange) {
        return exchange.getRequest().getURI().getHost();
    }

    private RibbonServer getRibbonServer(String serviceId, List<Server> servers) {
        Server server = LoadBalanceUtil.chooseByRoundRobin(serviceId, servers);
        if (server == null) {
            return null;
        }
        return new RibbonServer(
                serviceId
                , server
                , isSecure(server, serviceId)
                , serverIntrospector(serviceId).getMetadata(server)
        );
    }

    private ServerIntrospector serverIntrospector(String serviceId) {
        ServerIntrospector serverIntrospector = this.clientFactory.getInstance(serviceId,
                ServerIntrospector.class);
        if (serverIntrospector == null) {
            serverIntrospector = new DefaultServerIntrospector();
        }
        return serverIntrospector;
    }

    private boolean isSecure(Server server, String serviceId) {
        IClientConfig config = this.clientFactory.getClientConfig(serviceId);
        ServerIntrospector serverIntrospector = serverIntrospector(serviceId);
        return RibbonUtils.isSecure(config, serverIntrospector, server);
    }
}
