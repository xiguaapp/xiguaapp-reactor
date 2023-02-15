package com.cn.xiguaapp.r2dbc.orm.event;

import lombok.Getter;
import org.reactivestreams.Publisher;
import org.springframework.context.ApplicationEventPublisher;
import reactor.core.publisher.Mono;

/**
 * @author xiguaapp
 * @desc 异步事件监听响应式接口实现
 * @since 1.0 17:23
 */
public class DefaultAsyncEvent implements AsyncEvent {
    @Getter
    private Mono<Void> async = Mono.empty();

    @Override
    public synchronized void async(Publisher<?> publisher) {
        this.async = async.then(Mono.from(publisher).then());
    }

    @Override
    public Mono<Void> publish(ApplicationEventPublisher eventPublisher) {

        eventPublisher.publishEvent(this);

        return this.async;
    }
}
