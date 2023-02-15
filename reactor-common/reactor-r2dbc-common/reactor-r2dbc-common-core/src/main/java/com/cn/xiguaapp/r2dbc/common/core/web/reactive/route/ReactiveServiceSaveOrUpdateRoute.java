package com.cn.xiguaapp.r2dbc.common.core.web.reactive.route;

import com.cn.xiguaapp.r2dbc.common.core.web.reactive.handler.ReactiveServiceSaveOrUpdateHandler;
import com.cn.xiguaapp.r2dbc.common.core.web.reactive.path.BuildRequstApi;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

/**
 * @author xiguaapp
 * @desc
 * @since 1.0 21:09
 */
public interface ReactiveServiceSaveOrUpdateRoute<T,ID> extends BuildRequstApi {
    ReactiveServiceSaveOrUpdateHandler<T,ID> getReactiveServiceSaveOrUpdateHandler();
    @Bean
    default RouterFunction<ServerResponse> routeServiceCrud() {
        String path = getPathApi();
        return RouterFunctions
                .route()
                .POST(path+"/v1/saveOrUpdate", getReactiveServiceSaveOrUpdateHandler()::saveOrUpdate)
                .POST(path+"/v1/save", getReactiveServiceSaveOrUpdateHandler()::insert)
                .PUT(path+"/v1/updateById/{id}", getReactiveServiceSaveOrUpdateHandler()::update)
                .POST(path+"/v1/saveBatch", getReactiveServiceSaveOrUpdateHandler()::saveBatch)
                .build();
    }
}
