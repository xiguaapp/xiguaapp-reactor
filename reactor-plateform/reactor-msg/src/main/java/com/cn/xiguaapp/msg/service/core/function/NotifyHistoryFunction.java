package com.cn.xiguaapp.msg.service.core.function;

import com.cn.xiguaapp.msg.service.core.repository.NotifyHistoryRepository;
import com.cn.xiguaapp.notify.api.core.SerializableNotifierEvent;
import com.cn.xiguaapp.notify.api.entity.NotifyHistory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author xiguaapp
 * @package_name xiguaapp-reactor
 * @Date 十一月
 * @desc 消息通知记录
 */
@Component
public class NotifyHistoryFunction {
    @Autowired
    private NotifyHistoryRepository notifyHistoryRepository;

    /**
     * 将SerializableNotifierEvent转化为NotifyHistory并写入记录
     * 参数传输及参数格式 {@link Mono<SerializableNotifierEvent>}
     * @return 返回值格式{@link Mono<NotifyHistory>}
     */
    @Bean
    public Function<Mono<SerializableNotifierEvent>,Mono<NotifyHistory>>subscribeInsertNotifyHistory(){
        return flux->flux
                .flatMap(s->Mono.just(NotifyHistory.of(s)))
                .switchIfEmpty(Mono.error(new NullPointerException("数据为空")))
                .flatMap(notifyHistoryRepository::save);
    }

    /**
     * 批量删除
     * 参数格式{ids} {@link Flux<Long>}
     * @return
     */
    @Bean
    public Consumer<Flux<Long>>deleteByIds(){
        //判断ids是否存在 存在则删除 不存在不抛出任何错误
        return ids->ids
                .filterWhen(id-> notifyHistoryRepository
                        .findById(id).hasElement())
                .doOnNext(id->notifyHistoryRepository.deleteById(id));
    }

//    @Bean
//    public Function<Mono<PageParams<NotifyHistory>>, Flux<NotifyHistory>>findAllPage(){
//        return flux->flux.flatMap(entity->{
//            NotifyHistory notifyHistory = entity.mapToBean(NotifyHistory.class);
//        })
//    }
}
