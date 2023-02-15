/*
 *
 *       Copyright (C) <2018-2028>  <@author: xiguaapp @date: 2020/12/9 上午11:19 >
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

import com.cn.xiguaapp.common.gateway.bean.ApiParam;
import com.cn.xiguaapp.common.gateway.bean.TargetRoute;
import com.cn.xiguaapp.common.gateway.common.ServerWebExchangeUtil;
import org.springframework.web.server.ServerWebExchange;

import static com.cn.xiguaapp.common.gateway.bean.GatewayConstants.CACHE_ROUTE_INFO;

/**
 * @author xiguaapp
 */
public class GatewayForwardChooser extends BaseForwardChooser<ServerWebExchange> {

    @Override
    public ApiParam getApiParam(ServerWebExchange exchange) {
        return ServerWebExchangeUtil.getApiParam(exchange);
    }

    @Override
    public ForwardInfo getForwardInfo(ServerWebExchange exchange) {
        // 如果有异常，直接跳转到异常处理
        if (ServerWebExchangeUtil.getThrowable(exchange) != null) {
            return ForwardInfo.getErrorForwardInfo();
        }
        ForwardInfo forwardInfo = super.getForwardInfo(exchange);
        TargetRoute targetRoute = forwardInfo.getTargetRoute();
        exchange.getAttributes().put(CACHE_ROUTE_INFO, targetRoute.getRouteDefinition());
        return forwardInfo;
    }
}
