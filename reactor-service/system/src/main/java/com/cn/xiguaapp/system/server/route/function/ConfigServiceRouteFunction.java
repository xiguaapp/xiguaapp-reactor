/*
 *
 *       Copyright (C) <2018-2028>  <@author: xiguaapp @date: 2020/12/10 上午11:02 >
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

package com.cn.xiguaapp.system.server.route.function;

import com.cn.xiguaapp.r2dbc.common.api.crud.entity.QueryParamEntity;
import com.cn.xiguaapp.r2dbc.common.core.web.reactive.function.ReactiveCrudFunction;
import com.cn.xiguaapp.r2dbc.orm.rdb.mapping.ReactiveRepository;
import com.cn.xiguaapp.system.api.route.entity.ConfigServiceRoute;
import com.cn.xiguaapp.system.api.route.entity.param.InstanceDefinition;
import com.cn.xiguaapp.system.api.route.entity.param.ServiceSearchParam;
import com.cn.xiguaapp.system.server.route.repository.ConfigServiceRouteRepository;
import com.cn.ykyoung.server.route.ServiceRouteInfo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author xiguaapp
 * @desc
 * @since 1.0 11:02
 */
@Configuration
@AllArgsConstructor
@Slf4j
public class ConfigServiceRouteFunction implements ReactiveCrudFunction<ConfigServiceRoute, Long> {
    private final ConfigServiceRouteRepository repository;

    /**
     * 根据serviceId删除所有路由
     *
     * @return
     * @see ConfigServiceRoute#getServiceId()
     */
    @Bean
    public Function<String, Flux<Integer>> removeAllRoutes() {
        return serviceId -> repository
                .query(QueryParamEntity
                        .of("service_id", serviceId).noPaging())
                .flatMap(oo -> repository.deleteById(Mono.just(oo.getId())));
    }

    /**
     * 根据条件查询服务id
     *
     * @return
     */
    @Bean
    public Function<ServiceSearchParam, Flux<String>> listServiceInfo() {
        return param -> repository.query(QueryParamEntity
                .of().noPaging())
                .filterWhen(oo -> {
                    if (StringUtils.isBlank(param.getServiceId())) {
                        return Mono.just(true);
                    } else {
                        return Mono.just(oo.getServiceId().contains(param.getServiceId()));
                    }
                })
                .map(ConfigServiceRoute::getServiceId)
                .distinct();
    }

    /**
     * 保存路由接口
     *
     * @return
     */
    @Bean
    public BiFunction<Mono<ServiceRouteInfo>, Mono<InstanceDefinition>, Flux<Integer>> saveRoutes() {
        return (serviceRouteInfo, instance) -> instance
                .filterWhen(ins -> this.removeAllRoutes().apply(ins.getServiceId())
                        .hasElements()
                        .map(has -> !has))
                .zipWith(serviceRouteInfo)
                .flatMapMany((Tuple2<InstanceDefinition, ServiceRouteInfo> t) -> {
                    log.info("保存路由信息到数据库,instance:{}", instance);
                    InstanceDefinition t1 = t.getT1();
                    ServiceRouteInfo t2 = t.getT2();
                    return repository.insert(Flux.fromIterable(t2.getRouteDefinitionList()
                            .parallelStream()
                            .map(routeDefinition -> ConfigServiceRoute.of(t2.getServiceId(), routeDefinition))
                            .collect(Collectors.toList())));
                });
    }

    @Override
    public ReactiveRepository<ConfigServiceRoute, Long> getRepository() {
        return repository.getRepository();
    }
}
