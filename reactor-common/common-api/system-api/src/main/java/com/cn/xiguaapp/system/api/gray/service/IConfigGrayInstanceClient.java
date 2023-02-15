package com.cn.xiguaapp.system.api.gray.service;

import com.cn.xiguapp.common.core.constant.ServiceConstants;
import com.cn.xiguaapp.system.api.gray.entity.ConfigGrayInstance;
import feign.RequestLine;
import org.springframework.web.bind.annotation.PathVariable;
import reactivefeign.spring.config.ReactiveFeignClient;
import reactor.core.publisher.Mono;

/**
 * @author xiguaapp
 * @desc
 * @since 1.0 23:18
 */
@ReactiveFeignClient(name = ServiceConstants.SYSTEM_API,url = ServiceConstants.SYSTEM_API)
public interface IConfigGrayInstanceClient {
    @RequestLine("GET /config/gray/instance/{instanceId}")
    Mono<ConfigGrayInstance> findByInstance(@PathVariable("instanceId") String instanceId);
}
