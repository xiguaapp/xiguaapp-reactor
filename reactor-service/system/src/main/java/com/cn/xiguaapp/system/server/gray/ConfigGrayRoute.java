package com.cn.xiguaapp.system.server.gray;

import com.cn.xiguaapp.r2dbc.common.core.web.reactive.handler.ReactiveCrudHandler;
import com.cn.xiguaapp.r2dbc.common.core.web.reactive.path.BuildPath;
import com.cn.xiguaapp.r2dbc.common.core.web.reactive.route.ReactiveCrudRoute;
import com.cn.xiguaapp.system.api.gray.entity.ConfigGray;
import com.cn.xiguaapp.system.server.gray.handler.ConfigGrayHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

/**
 * @author xiguaapp
 * @desc
 * @since 1.0 17:09
 */
@Component
public class ConfigGrayRoute implements ReactiveCrudRoute<ConfigGray,Long> {
    private final ConfigGrayHandler configGrayHandler;

    public ConfigGrayRoute(ConfigGrayHandler configGrayHandler) {
        this.configGrayHandler = configGrayHandler;
    }

    @Override
    public BuildPath buildPath() {
        return BuildPath.builder().path("/configGray").build();
    }

    @Override
    public ReactiveCrudHandler<ConfigGray, Long> getReactiveCrudHandler() {
        return configGrayHandler;
    }

    @Bean
    public RouterFunction<ServerResponse> configGrayRoutes(){
        return RouterFunctions
                .route()
                .GET(getPathApi()+"/findByServiceId/{serviceId}",configGrayHandler::findByServiceId)
                .build();
    }
}
