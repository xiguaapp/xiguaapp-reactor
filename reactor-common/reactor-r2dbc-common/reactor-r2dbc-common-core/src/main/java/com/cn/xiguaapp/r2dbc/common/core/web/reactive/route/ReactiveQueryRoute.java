package com.cn.xiguaapp.r2dbc.common.core.web.reactive.route;

import com.cn.xiguaapp.r2dbc.common.core.web.reactive.handler.ReactiveQueryHandler;
import com.cn.xiguaapp.r2dbc.common.core.web.reactive.path.BuildRequstApi;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

/**
 * @author xiguaapp
 * @desc
 * @since 1.0 21:15
 */
public interface ReactiveQueryRoute<T,ID> extends BuildRequstApi {
    ReactiveQueryHandler<T,ID> getReactiveQueryHandler();
    @Bean
    default RouterFunction<ServerResponse> routeQuery(){
        String path = getPathApi();
        return RouterFunctions
                .route()
                .POST(path+("/v1/query/no-paging"),getReactiveQueryHandler()::quertPost)
                .POST(path+("/v1/queryPager/no-paging"),getReactiveQueryHandler()::queryPagerPost)
                .POST(path+("/v1/count"),getReactiveQueryHandler()::countPost)
                .GET(path+("/v1/query/no-paging"),getReactiveQueryHandler()::queryGet)
                .GET(path+("/v1/queryPager/no-paging"),getReactiveQueryHandler()::queryPagerGet)
                .GET(path+("/v1/count"),getReactiveQueryHandler()::countGet)
                .GET(path+("/v1/findById/{id}"),getReactiveQueryHandler()::findById)
                .build();
    }
}
