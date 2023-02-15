package com.cn.xiguaapp.common.gateway.controller;

import com.cn.xiguaapp.common.gateway.bean.ApiContext;
import com.cn.xiguaapp.common.gateway.common.ServerWebExchangeUtil;
import com.cn.xiguaapp.common.gateway.exception.ApiException;
import com.cn.xiguaapp.common.gateway.message.ErrorEnum;
import com.cn.xiguaapp.common.gateway.result.ResultExecutor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author xiguaapp
 */
@RestController
@RequestMapping
public class GatewayController {

    /**
     * 处理签名错误返回
     *
     * @param exchange exchange
     * @return 返回最终结果
     */
    @RequestMapping("/sop/validateError")
    public Mono<String> validateError(ServerWebExchange exchange) {
        Throwable throwable = ServerWebExchangeUtil.getThrowable(exchange);
        // 合并微服务传递过来的结果，变成最终结果
        ResultExecutor<ServerWebExchange, String> resultExecutor = ApiContext.getApiConfig().getGatewayResultExecutor();
        String gatewayResult = resultExecutor.buildErrorResult(exchange, throwable);
        exchange.getResponse().getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        return Mono.just(gatewayResult);
    }

    @RequestMapping("/xiguaapp/unknown")
    public Mono<String> unknown(ServerWebExchange exchange) {
        ApiException exception = ErrorEnum.ISV_INVALID_METHOD.getErrorMeta().getException();
        ResultExecutor<ServerWebExchange, String> resultExecutor = ApiContext.getApiConfig().getGatewayResultExecutor();
        String gatewayResult = resultExecutor.buildErrorResult(exchange, exception);
        exchange.getResponse().getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        return Mono.just(gatewayResult);
    }

}
