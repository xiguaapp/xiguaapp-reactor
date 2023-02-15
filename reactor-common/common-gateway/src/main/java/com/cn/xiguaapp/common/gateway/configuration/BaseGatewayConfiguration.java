/*
 *
 *       Copyright (C) <2018-2028>  <@author: xiguaapp @date: 2020/12/8 下午5:13 >
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

package com.cn.xiguaapp.common.gateway.configuration;

import com.cn.xiguaapp.common.gateway.bean.ApiConfig;
import com.cn.xiguaapp.common.gateway.common.GatewayModifyResponseGatewayFilter;
import com.cn.xiguaapp.common.gateway.exception.GatewayExceptionHandler;
import com.cn.xiguaapp.common.gateway.filter.IndexFilter;
import com.cn.xiguaapp.common.gateway.loadbalance.SopLoadBalancerClient;
import com.cn.xiguaapp.common.gateway.loadbalance.SopLoadBalancerClientFilter;
import com.cn.xiguaapp.common.gateway.manage.RouteRepositoryContext;
import com.cn.xiguaapp.common.gateway.filter.ParameterFormatterFilter;
import com.cn.xiguaapp.common.gateway.route.GatewayForwardChooser;
import com.cn.xiguaapp.common.gateway.route.GatewayRouteCache;
import com.cn.xiguaapp.common.gateway.route.GatewayRouteRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.gateway.config.LoadBalancerProperties;
import org.springframework.cloud.gateway.filter.LoadBalancerClientFilter;
import org.springframework.cloud.netflix.ribbon.SpringClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.web.reactive.result.view.ViewResolver;

import java.util.Collections;
import java.util.List;


/**
 * @author xiguaapp
 */
@Slf4j
public class BaseGatewayConfiguration extends AbstractConfiguration {

    public BaseGatewayConfiguration() {
        ApiConfig.getInstance().setUseGateway(true);
    }

    @Bean
    public IndexFilter indexFilter() {
        return new IndexFilter();
    }

    /**
     * 自定义异常处理[@@]注册Bean时依赖的Bean，会从容器中直接获取，所以直接注入即可
     *
     * @param viewResolversProvider viewResolversProvider
     * @param serverCodecConfigurer serverCodecConfigurer
     */
    @Primary
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public ErrorWebExceptionHandler sopErrorWebExceptionHandler(ObjectProvider<List<ViewResolver>> viewResolversProvider,
                                                             ServerCodecConfigurer serverCodecConfigurer) {

        GatewayExceptionHandler jsonExceptionHandler = new GatewayExceptionHandler();
        jsonExceptionHandler.setViewResolvers(viewResolversProvider.getIfAvailable(Collections::emptyList));
        jsonExceptionHandler.setMessageWriters(serverCodecConfigurer.getWriters());
        jsonExceptionHandler.setMessageReaders(serverCodecConfigurer.getReaders());
        return jsonExceptionHandler;
    }

    /**
     * 处理返回结果
     */
    @Bean
    GatewayModifyResponseGatewayFilter gatewayModifyResponseGatewayFilter() {
        return new GatewayModifyResponseGatewayFilter();
    }

    @Bean
    ParameterFormatterFilter parameterFormatterFilter() {
        return new ParameterFormatterFilter();
    }
//
//    @Bean
//    LimitFilter limitFilter() {
//        return new LimitFilter();
//    }
//
    @Bean
    GatewayRouteCache gatewayRouteCache(GatewayRouteRepository gatewayRouteRepository) {
        return new GatewayRouteCache(gatewayRouteRepository);
    }

    @Bean
    GatewayRouteRepository gatewayRouteRepository() {
        GatewayRouteRepository gatewayRouteRepository = new GatewayRouteRepository();
        RouteRepositoryContext.setRouteRepository(gatewayRouteRepository);
        return gatewayRouteRepository;
    }
//
    @Bean
    GatewayForwardChooser gatewayForwardChooser() {
        return new GatewayForwardChooser();
    }

    /**
     * 扩展默认的负载均衡选择，默认使用的是RibbonLoadBalancerClient
     * @param clientFactory
     * @return
     */
    @Bean
    LoadBalancerClient loadBalancerClient(SpringClientFactory clientFactory) {
        return new SopLoadBalancerClient(clientFactory);
    }

    /**
     * 扩展默认的负载均衡过滤器，默认是LoadBalancerClientFilter
     * @param sopLoadBalancerClient SopLoadBalancerClient
     * @param loadBalancerProperties loadBalancerProperties
     * @return
     */
    @Bean
    LoadBalancerClientFilter loadBalancerClientFilter(LoadBalancerClient sopLoadBalancerClient, LoadBalancerProperties loadBalancerProperties) {
        return new SopLoadBalancerClientFilter(sopLoadBalancerClient, loadBalancerProperties);
    }

}
