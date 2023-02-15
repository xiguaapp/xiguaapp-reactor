package com.cn.xiguaapp.r2dbc.common.core.web.reactive.handler;

import com.cn.xiguapp.common.core.utils.ClassUtils;
import com.cn.xiguaapp.r2dbc.common.api.crud.entity.PagerResult;
import com.cn.xiguaapp.r2dbc.common.api.crud.entity.QueryOperation;
import com.cn.xiguaapp.r2dbc.common.api.crud.entity.QueryParamEntity;
import com.cn.xiguaapp.r2dbc.common.core.FindClass;
import com.cn.xiguaapp.r2dbc.common.core.web.reactive.function.ReactiveQueryFunction;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

/**
 * @author xiguaapp
 * @desc
 * @since 1.0 17:58
 */
public interface ReactiveQueryHandler<T,ID> {
    ReactiveQueryFunction<T, ID> getReactiveQueryFunction();

    @QueryOperation(summary = "使用get方式分页动态查询(不返回总数)", description = "此操作不返回分页总数，如果需要获取全部数据 请设置参数paging=false")
    @ApiOperation(value = "此操作不返回分页总数，如果需要获取全部数据 请设置参数paging=false", notes = "query", response = QueryParamEntity.class)
    default Mono<ServerResponse> queryGet(ServerRequest request) {
        return request.bodyToMono(QueryParamEntity.class)
                .flatMap(qp -> ServerResponse
                        .ok()
                        .body(getReactiveQueryFunction()
                                .queryGet()
                                .apply(qp), FindClass.<T>findClass(getClass())));
    }

    @Operation(summary = "使用POST方式分页动态查询(不返回总数)",
            description = "此操作不返回分页总数,如果需要获取全部数据,请设置参数paging=false")
    default Mono<ServerResponse> quertPost(ServerRequest request) {
        return ServerResponse.ok()
                .body(getReactiveQueryFunction()
                                .quertPost()
                                .apply(request
                                        .bodyToMono(QueryParamEntity.class))
                        , FindClass
                                .findClass(getClass()));
    }

    @QueryOperation(summary = "使用GET方式分页动态查询,带分页查询结果")
    default Mono<ServerResponse> queryPagerGet(ServerRequest request) {
        return request.bodyToMono(QueryParamEntity.class)
                .flatMap(qp -> ServerResponse
                        .ok()
                        .body(getReactiveQueryFunction().queryPagerGet().apply(qp),
                                PagerResult.class));
    }

    @Operation(summary = "使用POST方式分页动态查询")
    default Mono<ServerResponse> queryPagerPost(ServerRequest request) {
        return ServerResponse.ok()
                .body(getReactiveQueryFunction()
                                .queryPagerPost()
                                .apply(request
                                        .bodyToMono(QueryParamEntity.class))
                        , PagerResult.class);
    }

    @QueryOperation(summary = "使用GET方式查询总数")
    default Mono<ServerResponse> countGet(ServerRequest request) {
        return request
                .bodyToMono(QueryParamEntity.class)
                .flatMap(qp -> ServerResponse.ok()
                        .body(getReactiveQueryFunction()
                                .countGet()
                                .apply(qp), Integer.class));
    }

    @Operation(summary = "使用POST方式查询总数")
    default Mono<ServerResponse> countPost(ServerRequest request) {
        return ServerResponse
                .ok()
                .body(getReactiveQueryFunction()
                                .count()
                                .apply(request.bodyToMono(QueryParamEntity.class))
                        , Integer.class);
    }

    @Operation(summary = "根据ID查询")
    default Mono<ServerResponse> findById(ServerRequest request) {
        return ServerResponse
                .ok()
                .body(getReactiveQueryFunction()
                                .getById()
                                .apply(Mono.just((ID) request.pathVariable("id")))
                        , ClassUtils.getGenericType(getClass()));
    }
}
