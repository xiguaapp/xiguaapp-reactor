package com.cn.xiguaapp.msg.service.core.handler;

import com.cn.xiguaapp.notify.api.entity.NotifyHistory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author xiguaapp
 * @package_name xiguaapp-reactor
 * @Date 十一月
 * @desc
 */
@Component
public class NotifyHistoryHandler {
    @Autowired
    @Lazy
    public Function<Mono<NotifyHistory>,Mono<NotifyHistory>> insert;
    @Autowired
    public Consumer<Flux<Long>>deleteByIds;

//    public Mono<ServerResponse>insertHistory(ServerRequest request){
//        return insert
//                .apply(request.bodyToMono(NotifyHistory.class))
//                .flatMap(history->ServerResponse.ok().bodyValue("添加成功"))
//                .switchIfEmpty(ServerResponse.badRequest()
//                        .contentType(APPLICATION_JSON).bodyValue("添加失败"))
//                .subscribeOn(Schedulers.elastic());
//    }

//    public Mono<ServerResponse>deleteByIdsHistorys(ServerRequest request){
//        return ServerResponse.;
//    }

}
