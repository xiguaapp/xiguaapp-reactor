package com.cn.xiguaapp.system.server.isv.handler;

import com.cn.xiguapp.common.core.utils.BeansUtils;
import com.cn.xiguapp.common.core.utils.RedisUtils;
import com.cn.xiguaapp.system.api.isv.entity.IsvInfo;
import com.cn.xiguaapp.system.api.isv.entity.param.ChannelMsg;
import com.cn.xiguaapp.system.api.isv.entity.param.IsvDefinition;
import com.cn.xiguaapp.system.api.isv.entity.param.IsvPut;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author xiguaapp
 * @Date 2020/10/10
 * @desc isv缓存管理
 */
@Component
@Slf4j
public class CacheIsvHandler {
    @Autowired
    private RedisUtils<IsvDefinition> redisUtils;
    @Autowired(required = false)
    private Supplier<Flux<IsvInfo>> listIsvDetail;

    public Mono<ServerResponse> update(ServerRequest request) {
        Mono<IsvDefinition> isvDefinitionMono = request.bodyToMono(IsvDefinition.class);
        return ServerResponse.ok().body(Mono.<Void>defer(()->
            isvDefinitionMono.flatMap(oos->{
                Mono.fromCallable(()->redisUtils.set(OFBUILD_KEY.apply(oos.getAppKey()),oos));
                return Mono.empty();
            })
        ),Void.class);
    }
    private Consumer<Flux<IsvDefinition>>updateIsvDefini(){
        return f->f.subscribe(oos->redisUtils.set(OFBUILD_KEY.apply(oos.getAppKey()),oos));
    }

    public Mono<ServerResponse> removeByAppKey(String appKey) {
        return ServerResponse.ok().body(Mono.<Void>defer(()->{
            redisUtils.del(appKey);
            return Mono.empty();
        }),Void.class);
    }
    public Mono<ServerResponse>remove(ServerRequest request){
        return this.removeByAppKey(request.pathVariable("appKey"));
    }

    public Mono<ServerResponse> updateIsv(ServerRequest request) {
//
        return request.bodyToMono(IsvPut.class)
                .flatMap(oo->ServerResponse.ok()
                        .body(redisUtils.set(oo.getAppKey(),new IsvDefinition()
                                .setSecret(oo.getSecret())),Boolean.class));
    }


    private static final Function<String,String> OFBUILD_KEY = it->String.format("isv:%s",it);

    public Mono<ServerResponse> listIsvDetail(ServerRequest request){
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                .body(listIsvDetail.get(), IsvInfo.class).log();
    }

    public Mono<ServerResponse>process(ServerRequest request){
        return request.bodyToMono(ChannelMsg.class)
                .flatMap(pp->ServerResponse.ok()
                        .body(this.processAA(pp),Void.class));
    }

    public Mono<ServerResponse> getIsv(ServerRequest request){
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                .body(listIsvDetail.get().filter(f->f.getAppKey().equals(request.pathVariable("appKey")))
                        , IsvDefinition.class)
                .log();
    }


    /**
     * 加载
     * @return
     */
    public Mono<ServerResponse> load(ServerRequest request) {
        return ServerResponse.ok().body(Mono.fromCallable(() -> {
            this.updateIsvDefini().accept(listIsvDetail.get()
                    .map(pp -> BeansUtils.copy(pp, new IsvDefinition())));
            return Mono.empty();
        }),Void.class);
    }

    /**
     * 更新或者删除操作
     * @param channelMsg 渠道参数
     */
    public boolean processAA(ChannelMsg channelMsg) {
        IsvDefinition isvDefinition = channelMsg.toObject(IsvDefinition.class);
        switch (channelMsg.getOperation()){
            case "update","add"->{
                log.info("更新ISV信息，isvDefinition:{}", isvDefinition);
                updateIsvDefini().accept(Flux.just(isvDefinition));
            }
            case "remove"->{
                log.info("删除ISV，isvDefinition:{}", isvDefinition);
                removeByAppKey(isvDefinition.getAppKey());
            }
            default -> {}
        }
        return true;
    }
}
