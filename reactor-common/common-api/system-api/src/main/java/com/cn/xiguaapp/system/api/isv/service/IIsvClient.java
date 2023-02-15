package com.cn.xiguaapp.system.api.isv.service;

import com.cn.xiguapp.common.core.constant.ServiceConstants;
import com.cn.xiguaapp.system.api.isv.entity.param.Isv;
import com.cn.xiguaapp.system.api.isv.entity.param.IsvPut;
import feign.RequestLine;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import reactivefeign.spring.config.ReactiveFeignClient;
import reactor.core.publisher.Mono;

/**
 * @author xiguaapp
 * @Date 2020/10/12
 * @desc
 */
@ReactiveFeignClient(name = ServiceConstants.SYSTEM_API,url = ServiceConstants.SYSTEM_API)
public interface IIsvClient {

    @RequestLine("PUT /isv/load")
    Mono<Void> load();

    @RequestLine("GET /isv/get_isv/{appKey}")
    Mono<Isv> getIsv(@PathVariable("appKey") String appKey);

    @RequestLine("PUT /isv/update")
    Mono<Void>update(@RequestBody IsvPut isvPut);
}
