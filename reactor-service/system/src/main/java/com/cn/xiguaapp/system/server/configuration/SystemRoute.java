/*
 *
 *       Copyright (C) <2018-2028>  <@author: xiguaapp @date: 2020/12/10 下午8:36 >
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

package com.cn.xiguaapp.system.server.configuration;

import com.cn.xiguaapp.system.server.isv.handler.CacheIsvHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

/**
 * @author xiguaapp
 */
@Component
public class SystemRoute {


    @Bean
    public RouterFunction<ServerResponse> isvRoute(CacheIsvHandler isvHandler){
        return RouterFunctions
                .route()
                .GET("/isv/list",isvHandler::listIsvDetail)
                .PUT("/isv/load",isvHandler::load)
                .POST("/isv/process",isvHandler::process)
                .GET("/isv/get_isv/{appKey}",isvHandler::getIsv)
                .PUT("/isv/update",isvHandler::updateIsv)
                .build();
    }

}