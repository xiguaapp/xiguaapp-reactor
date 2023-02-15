/*
 *
 *       Copyright (C) <2018-2028>  <@author: xiguaapp @date: 2020/12/9 上午11:22 >
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

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.gateway.config.LoadBalancerProperties;
import org.springframework.cloud.gateway.filter.LoadBalancerClientFilter;
import org.springframework.web.server.ServerWebExchange;

import java.net.URI;

import static com.cn.xiguaapp.common.gateway.bean.GatewayConstants.TARGET_SERVICE;
import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR;

/**
 * 扩展负载均衡过滤器
 * @author xiguaapp
 */
public class SopLoadBalancerClientFilter extends LoadBalancerClientFilter {
    public SopLoadBalancerClientFilter(LoadBalancerClient loadBalancer, LoadBalancerProperties properties) {
        super(loadBalancer, properties);
    }

    @Override
    protected ServiceInstance choose(ServerWebExchange exchange) {
        ServiceInstance serviceInstance;
        if (loadBalancer instanceof SopLoadBalancerClient) {
            SopLoadBalancerClient sopLoadBalancerClient = (SopLoadBalancerClient)loadBalancer;
            serviceInstance =  sopLoadBalancerClient.choose(((URI) exchange.getAttribute(GATEWAY_REQUEST_URL_ATTR)).getHost(), exchange);
        } else {
            serviceInstance = super.choose(exchange);
        }
        exchange.getAttributes().put(TARGET_SERVICE, serviceInstance);
        return serviceInstance;
    }
}
