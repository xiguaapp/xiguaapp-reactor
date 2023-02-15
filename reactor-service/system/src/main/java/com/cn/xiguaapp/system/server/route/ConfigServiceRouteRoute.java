package com.cn.xiguaapp.system.server.route;

import com.cn.xiguaapp.r2dbc.common.core.web.reactive.handler.ReactiveCrudHandler;
import com.cn.xiguaapp.r2dbc.common.core.web.reactive.path.BuildPath;
import com.cn.xiguaapp.r2dbc.common.core.web.reactive.route.ReactiveCrudRoute;
import com.cn.xiguaapp.system.api.route.entity.ConfigServiceRoute;
import com.cn.xiguaapp.system.server.route.handler.ConfigServiceRouteHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

/**
 * @author xiguaapp
 * @desc
 * @since 1.0 18:12
 */
@Component
public class ConfigServiceRouteRoute implements ReactiveCrudRoute<ConfigServiceRoute,Long> {
    private final ConfigServiceRouteHandler configServiceRouteHandler;

    public ConfigServiceRouteRoute(ConfigServiceRouteHandler configServiceRouteHandler) {
        this.configServiceRouteHandler = configServiceRouteHandler;
    }

    @Override
    public BuildPath buildPath() {
        return BuildPath.builder().path("/config/service/route").build();
    }

    @Override
    public ReactiveCrudHandler<ConfigServiceRoute, Long> getReactiveCrudHandler() {
        return configServiceRouteHandler;
    }

    @Bean
    public RouterFunction<ServerResponse> configServiceRoute(ConfigServiceRouteHandler handler){
        return RouterFunctions
                .route()
                .DELETE(getPathApi()+"/delete/{serviceId}",handler::removeAllRoutes)
                .POST(getPathApi()+"/saveRoute",handler::saveRoutes)
                .build();
    }
}
