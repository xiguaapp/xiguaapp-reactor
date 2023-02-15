/*
 *
 *       Copyright (C) <2018-2028>  <@author: xiguaapp @date: 2020/12/10 下午4:25 >
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

package com.cn.xiguaapp.system.api.route.service;

import com.cn.xiguapp.common.core.constant.ServiceConstants;
import com.cn.xiguaapp.system.api.gray.param.ServiceRouteInfoAndInstance;
import feign.RequestLine;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import reactivefeign.spring.config.ReactiveFeignClient;
import reactor.core.publisher.Mono;

/**
 * @author xiguaapp
 * @desc
 * @since 1.0 16:25
 */
@ReactiveFeignClient(name = ServiceConstants.SYSTEM_API)
public interface IConfigServiceRouteClient {
    @RequestLine("DELETE /config/route/service/delete/{serviceId}")
    Mono<Void>deleteByServiceId(@PathVariable("serviceId")String serviceId);
    @RequestLine("POST /config/service/route/saveRoute")
    Mono<Void>saveRoutes(@RequestBody ServiceRouteInfoAndInstance instance);
}
