package com.cn.xiguaapp.r2dbc.common.core.web.reactive.route;

import com.cn.xiguaapp.r2dbc.common.core.web.reactive.handler.ReactiveServiceQueryHandler;
import com.cn.xiguaapp.r2dbc.common.core.web.reactive.path.BuildRequstApi;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

/**
 * @author xiguaapp
 * @desc
 * @since 1.0 21:23
 */
public interface ReactiveServiceQueryRoute<T,ID> extends BuildRequstApi {
    ReactiveServiceQueryHandler<T,ID> getReactiveServiceQueryHandler();
    @Bean
    default RouterFunction<ServerResponse> routeServiceQuery(){
        String path = (getPathApi());
        return RouterFunctions
                .route()
                .POST(path+"/v1/query/no-paging",getReactiveServiceQueryHandler()::quertPost)
                .POST(path+"/v1/queryPager/no-paging",getReactiveServiceQueryHandler()::queryPagerPost)
                .POST(path+"/v1/count",getReactiveServiceQueryHandler()::countPost)
                .GET(path+"/v1/query/no-paging",getReactiveServiceQueryHandler()::queryGet)
                .GET(path+"/v1/queryPager/no-paging",getReactiveServiceQueryHandler()::queryPagerGet)
                .GET(path+"/v1/count",getReactiveServiceQueryHandler()::countGet)
                .GET(path+"/v1/findById/{id}",getReactiveServiceQueryHandler()::findById)
                .build();
    }
}
