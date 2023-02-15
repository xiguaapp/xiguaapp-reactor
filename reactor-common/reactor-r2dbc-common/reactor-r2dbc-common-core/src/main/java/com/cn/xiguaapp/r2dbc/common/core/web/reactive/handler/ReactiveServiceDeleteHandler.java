package com.cn.xiguaapp.r2dbc.common.core.web.reactive.handler;

import com.cn.xiguaapp.r2dbc.common.core.FindClass;
import com.cn.xiguaapp.r2dbc.common.core.web.reactive.function.ReactiveServiceDeleteFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

/**
 * @author xiguaapp
 * @desc
 * @since 1.0 16:33
 */
public interface ReactiveServiceDeleteHandler<T,ID> {
    ReactiveServiceDeleteFunction<T,ID> getReactiveServiceDeleteFunction();

    default Mono<ServerResponse> deleteById(ServerRequest request){
        return ServerResponse
                .ok()
                .body(getReactiveServiceDeleteFunction()
                                .delete()
                                .apply(Mono.just((ID) request.pathVariable("id")))
                        , FindClass.findClass(this.getClass()));
    }
    default Mono<ServerResponse>deleteBatchById(ServerRequest request){
        return ServerResponse
                .ok()
                .body(getReactiveServiceDeleteFunction()
                                .deleteBatch()
                                .apply(request
                                        .bodyToFlux(FindClass.findClass(this.getClass(),1)))
                        ,FindClass.findClass(this.getClass()));
    }

}
