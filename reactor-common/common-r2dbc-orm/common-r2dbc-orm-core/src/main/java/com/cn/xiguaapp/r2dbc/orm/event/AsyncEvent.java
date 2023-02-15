package com.cn.xiguaapp.r2dbc.orm.event;

import org.reactivestreams.Publisher;
import org.springframework.context.ApplicationEventPublisher;
import reactor.core.publisher.Mono;

/**
 * @author xiguaapp
 * @desc 异步事件 使用响应式编程进行事件监听的时候 一定必须使用此事件接口
 * @since 1.0 17:21
 */
public interface AsyncEvent {
    /**
     * 获取一个异步任务
     * @return Async void
     */
    Mono<Void> getAsync();

    /**
     * 注册一个异步任务
     *
     * @param publisher 异步任务
     */
    void async(Publisher<?> publisher);

    /**
     * 推送事件到 ApplicationEventPublisher
     *
     * @param eventPublisher ApplicationEventPublisher
     * @return async void
     */
    Mono<Void> publish(ApplicationEventPublisher eventPublisher);
}
