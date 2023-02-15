package com.cn.xiguaapp.r2dbc.common.core.web.reactive.route;

import com.cn.xiguaapp.r2dbc.common.core.web.reactive.handler.ReactiveServiceDeleteHandler;
import com.cn.xiguaapp.r2dbc.common.core.web.reactive.path.BuildRequstApi;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

/**
 * @author xiguaapp
 * @desc
 * @since 1.0 21:14
 */
public interface ReactiveServiceDeleteRoute<T,ID> extends BuildRequstApi {
    ReactiveServiceDeleteHandler<T,ID> getReactiveServiceDeleteHandler();
    @Bean
    default RouterFunction<ServerResponse> routeServiceDelete(){
        String path = (getPathApi());
        return RouterFunctions
                .route()
                .DELETE(path+("/delById/{id}"),getReactiveServiceDeleteHandler()::deleteById)
                .POST(path+("/deleteBatch"),getReactiveServiceDeleteHandler()::deleteBatchById)
                .build();
    }
}
