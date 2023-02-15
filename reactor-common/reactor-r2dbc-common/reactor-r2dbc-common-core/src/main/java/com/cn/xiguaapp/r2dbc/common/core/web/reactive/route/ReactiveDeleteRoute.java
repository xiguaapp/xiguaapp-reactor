package com.cn.xiguaapp.r2dbc.common.core.web.reactive.route;

import com.cn.xiguaapp.r2dbc.common.core.web.reactive.handler.ReactiveDeleteHandler;
import com.cn.xiguaapp.r2dbc.common.core.web.reactive.path.BuildRequstApi;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

/**
 * @author xiguaapp
 * @desc
 * @since 1.0 21:10
 */
public interface ReactiveDeleteRoute<T,ID> extends BuildRequstApi {
    ReactiveDeleteHandler<T,ID> getReactiveDeleteHandler();
    @Bean
    default RouterFunction<ServerResponse>routeDelete(){
        String path = getPathApi();
        return RouterFunctions
                .route()
                .DELETE(path+("/delById/{id}"),getReactiveDeleteHandler()::deleteById)
                .POST(path+("/deleteBatch"),getReactiveDeleteHandler()::deleteBatchById)
                .build();
    }
}
