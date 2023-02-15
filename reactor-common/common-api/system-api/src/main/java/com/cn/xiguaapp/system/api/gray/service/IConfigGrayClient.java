package com.cn.xiguaapp.system.api.gray.service;

import com.cn.xiguapp.common.core.constant.ServiceConstants;
import com.cn.xiguaapp.system.api.gray.entity.ConfigGray;
import feign.RequestLine;
import org.springframework.web.bind.annotation.PathVariable;
import reactivefeign.spring.config.ReactiveFeignClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author xiguaapp
 * @desc
 * @since 1.0 23:24
 */
@ReactiveFeignClient(name = ServiceConstants.SYSTEM_API,url = ServiceConstants.SYSTEM_API)
public interface IConfigGrayClient {
    @RequestLine("GET /config/gray/findAll")
    Flux<ConfigGray>findAll();
    @RequestLine("GET /config/gray/findByServiceId/{serviceId}")
    Mono<ConfigGray> findByServiceId(@PathVariable("serviceId") String serviceId);
}
