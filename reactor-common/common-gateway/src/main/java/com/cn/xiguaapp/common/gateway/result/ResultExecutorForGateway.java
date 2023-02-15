package com.cn.xiguaapp.common.gateway.result;

import org.springframework.web.server.ServerWebExchange;

/**
 * @author xiguaapp
 * @Date 2020/10/12
 * @desc
 */
public interface ResultExecutorForGateway extends ResultExecutor<ServerWebExchange, String>  {
}
