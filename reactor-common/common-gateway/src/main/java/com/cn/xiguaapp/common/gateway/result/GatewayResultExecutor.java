/*
 *
 *       Copyright (C) <2018-2028>  <@author: xiguaapp @date: 2020/12/8 下午4:26 >
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

package com.cn.xiguaapp.common.gateway.result;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cn.xiguaapp.common.gateway.bean.ApiParam;
import com.cn.xiguaapp.common.gateway.bean.GatewayConstants;
import com.cn.xiguaapp.common.gateway.common.ServerWebExchangeUtil;
import com.cn.xiguaapp.common.gateway.exception.ApiException;
import com.cn.xiguaapp.common.gateway.interceptor.DefaultRouteInterceptorContext;
import com.cn.xiguaapp.common.gateway.interceptor.RouteInterceptorContext;
import com.cn.xiguaapp.common.gateway.message.ErrorEnum;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.http.HttpStatus;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.UriUtils;
import com.cn.xiguaapp.common.gateway.message.Error;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Locale;

/**
 * @author xiguaapp
 * @desc 网关处理结果合并
 * @since 1.0 16:26
 */
public class GatewayResultExecutor extends BaseExecutorAdapter<ServerWebExchange,String> implements ResultExecutorForGateway {
    @Override
    public int getResponseStatus(ServerWebExchange exchange) {
        HttpStatus statusCode = exchange.getResponse().getStatusCode();
        int responseStatus = statusCode.value();
        List<String> errorCodeList = exchange.getResponse().getHeaders().get(GatewayConstants.X_SERVICE_ERROR_CODE);
        if (!CollectionUtils.isEmpty(errorCodeList)) {
            String errorCode = errorCodeList.get(0);
            responseStatus = Integer.parseInt(errorCode);
        }
        return responseStatus;
    }

    @Override
    public String getResponseErrorMessage(ServerWebExchange exchange) {
        String errorMsg = null;
        List<String> errorMessageList = exchange.getResponse().getHeaders().get(GatewayConstants.X_SERVICE_ERROR_MESSAGE);
        if (!CollectionUtils.isEmpty(errorMessageList)) {
            errorMsg = errorMessageList.get(0);
        }
        if (StringUtils.hasText(errorMsg)) {
            errorMsg = UriUtils.decode(errorMsg, StandardCharsets.UTF_8);
        }
        exchange.getResponse().getHeaders().remove(GatewayConstants.X_SERVICE_ERROR_MESSAGE);
        return errorMsg;
    }

    @Override
    public ApiParam getApiParam(ServerWebExchange exchange) {
        return ServerWebExchangeUtil.getApiParam(exchange);
    }

    @Override
    protected Locale getLocale(ServerWebExchange exchange) {
        return exchange.getLocaleContext().getLocale();
    }

    @Override
    protected RouteInterceptorContext getRouteInterceptorContext(ServerWebExchange exchange) {
        return (RouteInterceptorContext) exchange.getAttributes().get(GatewayConstants.CACHE_ROUTE_INTERCEPTOR_CONTEXT);
    }

    @Override
    protected void bindRouteInterceptorContextProperties(RouteInterceptorContext routeInterceptorContext, ServerWebExchange requestContext) {
        ServiceInstance serviceInstance = requestContext.getAttribute(GatewayConstants.TARGET_SERVICE);
        DefaultRouteInterceptorContext context = (DefaultRouteInterceptorContext) routeInterceptorContext;
        context.setServiceInstance(serviceInstance);
    }

    @Override
    public String buildErrorResult(ServerWebExchange exchange, Throwable ex) {
        Locale locale = getLocale(exchange);
        Error error;
        if (ex instanceof ApiException) {
            ApiException apiException = (ApiException) ex;
            error = apiException.getError(locale);
        } else {
            error = ErrorEnum.ISP_UNKNOWN_ERROR.getErrorMeta().getError(locale);
        }
        JSONObject jsonObject = (JSONObject) JSON.toJSON(error);
        return this.merge(exchange, jsonObject);
    }

}
