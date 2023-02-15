package com.cn.xiguaapp.r2dbc.common.core.web.reactive.handler;

import com.cn.xiguaapp.r2dbc.common.core.FindClass;
import com.cn.xiguaapp.r2dbc.common.core.web.reactive.function.ReactiveServiceSaveOrUpdateFunction;
import com.cn.xiguaapp.r2dbc.orm.rdb.mapping.defaults.SaveResult;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author xiguaapp
 * @desc
 * @since 1.0 18:57
 */
public interface ReactiveServiceSaveOrUpdateHandler<T,ID> {
    ReactiveServiceSaveOrUpdateFunction<T,ID> getServiceSaveOrUpdateFunction();

    @Operation(summary = "保存数据", description = "如果传入了id,并且对应数据存在,则尝试覆盖,不存在则新增.")
    default Mono<ServerResponse>saveOrUpdate(ServerRequest request){
        return request
                .bodyToMono(FindClass.<T>findClass(this.getClass()))
                .switchIfEmpty(Mono.error(new RuntimeException("请确认参数后重试!")))
                .flatMap(oo->ServerResponse.ok().body(getServiceSaveOrUpdateFunction()
                        .saveOrUpdate()
                        .apply(Flux.just(oo)), SaveResult.class));
    }
    @Operation(summary = "批量新增数据")
    default Mono<ServerResponse>saveBatch(ServerRequest request){
        return ServerResponse.ok()
                .body(getServiceSaveOrUpdateFunction()
                        .saveBatch()
                        .apply(request.bodyToFlux(FindClass.findClass(this.getClass())))
                        ,Integer.class);
    }
    @Operation(summary = "新增单个数据,并返回新增后的数据.")
    default Mono<ServerResponse>insert(ServerRequest request){
        return ServerResponse
                .ok()
                .body(getServiceSaveOrUpdateFunction()
                        .insert()
                        .apply(request.bodyToMono(FindClass.findClass(this.getClass())))
                        ,FindClass.findClass(this.getClass()));
    }
    @Operation(summary = "根据ID修改数据")
    default Mono<ServerResponse>update(ServerRequest request){
        return ServerResponse
                .ok()
                .body(getServiceSaveOrUpdateFunction()
                        .update()
                        .apply((ID) request.pathVariable("id")
                                ,request.bodyToMono(FindClass.findClass(this.getClass())))
                        ,Boolean.class);
    }
}
