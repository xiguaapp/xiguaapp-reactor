package com.cn.xiguaapp.system.server.gray.handler;

import com.cn.xiguaapp.r2dbc.common.core.web.reactive.function.ReactiveCrudFunction;
import com.cn.xiguaapp.r2dbc.common.core.web.reactive.handler.ReactiveCrudHandler;
import com.cn.xiguaapp.system.api.gray.entity.ConfigGray;
import com.cn.xiguaapp.system.server.gray.function.ConfigGrayFunction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;

/**
 * @author xiguaapp
 * @desc
 * @since 1.0 01:11
 */
@Component
public class ConfigGrayHandler implements ReactiveCrudHandler<ConfigGray,Long> {
    @Autowired
    private ConfigGrayFunction configGrayFunction;

    public Flux<ServerResponse> findByServiceId(ServerRequest request){
        return ServerResponse.ok()
                .body(configGrayFunction.findConfigGrayByServiceId()
                        .apply(request.pathVariable("serviceId"))
                        ,ConfigGray.class)
                .flux();
    }

    @Override
    public ReactiveCrudFunction<ConfigGray, Long> getReactiveCrudFunction() {
        return configGrayFunction;
    }
}
