/*
 *
 *       Copyright (C) <2018-2028>  <@author: xiguaapp @date: 2020/12/10 下午7:48 >
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

package com.cn.xiguaapp.system.server.route.handler;

import com.cn.xiguaapp.r2dbc.common.core.web.reactive.function.ReactiveCrudFunction;
import com.cn.xiguaapp.r2dbc.common.core.web.reactive.handler.ReactiveCrudHandler;
import com.cn.xiguaapp.system.api.gray.param.ServiceRouteInfoAndInstance;
import com.cn.xiguaapp.system.api.route.entity.ConfigServiceRoute;
import com.cn.xiguaapp.system.server.route.function.ConfigServiceRouteFunction;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

/**
 * @author xiguaapp
 * @desc
 * @since 1.0 19:48
 */
@Component
public class ConfigServiceRouteHandler implements ReactiveCrudHandler<ConfigServiceRoute,Long> {
    private final ConfigServiceRouteFunction serviceRouteFunction;

    public ConfigServiceRouteHandler(ConfigServiceRouteFunction serviceRouteFunction) {
        this.serviceRouteFunction = serviceRouteFunction;
    }

    public Mono<ServerResponse> removeAllRoutes(ServerRequest request) {
        return Mono.defer(() -> serviceRouteFunction.removeAllRoutes()
                .apply(request.pathVariable("serviceId")).hasElements()
                .flatMap(f -> {
                    if (f) {
                        return ServerResponse.ok().body("操作成功", String.class);
                    }
                    return ServerResponse.badRequest().body("操作失败", String.class);
                }));
    }

    public Mono<ServerResponse> saveRoutes(ServerRequest request) {
        return request.bodyToMono(ServiceRouteInfoAndInstance.class)
                .flatMap(oo->serviceRouteFunction.saveRoutes()
                        .apply(oo.getInfo()
                                ,oo.getInstanceDefinition())
                        .hasElements())
                .flatMap(oot->{
                    if (oot){
                       return ServerResponse.ok().build();
                    }
                    return ServerResponse.badRequest()
                            .bodyValue("保存失败");
                });

    }

    @Override
    public ReactiveCrudFunction<ConfigServiceRoute, Long> getReactiveCrudFunction() {
        return serviceRouteFunction;
    }
}
