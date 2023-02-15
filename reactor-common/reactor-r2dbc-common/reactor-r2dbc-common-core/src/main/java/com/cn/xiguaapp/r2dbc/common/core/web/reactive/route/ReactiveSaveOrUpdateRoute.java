package com.cn.xiguaapp.r2dbc.common.core.web.reactive.route;

import com.cn.xiguaapp.r2dbc.common.core.web.reactive.handler.ReactiveSaveOrUpdateHandler;
import com.cn.xiguaapp.r2dbc.common.core.web.reactive.path.BuildRequstApi;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

/**
 * @author xiguaapp
 * @desc
 * @since 1.0 20:06
 */
public interface ReactiveSaveOrUpdateRoute<T,ID> extends BuildRequstApi {
    ReactiveSaveOrUpdateHandler<T,ID> getReactiveSaveOrUpdateHandler();
    @Bean
    default RouterFunction<ServerResponse>routeCrud() {
        String path = (getPathApi());
        return RouterFunctions
                .route()
                .POST(path+("/v1/saveOrUpdate"), getReactiveSaveOrUpdateHandler()::saveOrUpdate)
                .POST(path+("/v1/save"), getReactiveSaveOrUpdateHandler()::insert)
                .PUT(path+("/v1/updateById/{id}"), getReactiveSaveOrUpdateHandler()::update)
                .POST(path+("/v1/saveBatch"), getReactiveSaveOrUpdateHandler()::saveBatch)
                .build();
    }
}
