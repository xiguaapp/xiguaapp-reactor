/*
 *
 *       Copyright (C) <2018-2028>  <@author: xiguaapp @date: 2020/12/9 上午11:10 >
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

package com.cn.xiguaapp.common.gateway.filter;

import com.cn.xiguaapp.common.gateway.bean.ApiParam;
import com.cn.xiguaapp.common.gateway.bean.ParamNames;
import com.cn.xiguaapp.common.gateway.common.ServerWebExchangeUtil;
import com.cn.xiguaapp.common.gateway.param.ParameterFormatter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static com.cn.xiguaapp.common.gateway.bean.Orders.PARAMETER_FORMATTER_FILTER_ORDER;

/**
 * @author xiguaapp
 */
@Slf4j
public class ParameterFormatterFilter implements GlobalFilter, Ordered {

    @Autowired(required = false)
    private ParameterFormatter parameterFormatter;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ApiParam apiParam = ServerWebExchangeUtil.getApiParam(exchange);
        if (apiParam == null) {
            return chain.filter(exchange);
        }
        // 校验成功后进行参数转换
        if (parameterFormatter != null) {
            ServerWebExchange formatExchange = ServerWebExchangeUtil.format(
                    exchange
                    , apiParam
                    , parameterFormatter::format
                    , httpHeaders -> httpHeaders.set(ParamNames.HEADER_VERSION_NAME, apiParam.fetchVersion()));
            return chain.filter(formatExchange);
        }
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return PARAMETER_FORMATTER_FILTER_ORDER;
    }
}
