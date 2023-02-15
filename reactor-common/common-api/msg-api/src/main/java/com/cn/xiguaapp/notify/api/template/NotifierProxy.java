package com.cn.xiguaapp.notify.api.template;

import com.cn.xiguaapp.notify.api.constant.NotifyType;
import com.cn.xiguaapp.notify.api.event.NotifierEvent;
import com.cn.xiguaapp.notify.api.notify.Notifier;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

import javax.annotation.Nonnull;
import java.util.Map;

/**
 * @author xiguaapp
 * @project-name xiguaapp-reactor
 * @Date 2020/10/27
 * @desc
 */
@AllArgsConstructor
public abstract class NotifierProxy<T extends Template> implements Notifier<T> {
    private final Notifier<T> target;

    /**
     *  获取通知器id
     * @return
     */
    @Override
    public Long getNotifierId() {
        return target.getNotifierId();
    }

    /**
     * 获取通知类型
     * @return
     */
    @Nonnull
    @Override
    public NotifyType getType() {
        return target.getType();
    }

    /**
     * 获取服务提供商
     * @return
     */
    @Nonnull
    @Override
    public Provider getProvider() {
        return target.getProvider();
    }

    /**
     * 根据模版id发送信息
     * @param templateId 模版ID
     * @param context    上下文
     * @return
     */
    @Nonnull
    @Override
    public Mono<Void> send(@Nonnull String templateId, Map<String,Object> context) {
        return target
                .send(templateId,context)
                .switchIfEmpty(Mono.defer(()->onSuccess(templateId, context)))
                .onErrorResume(err->onError(templateId,context,err)
                        .then(Mono.error(err)));
    }

    /**
     * 根据模版发送消息
     * @param template 模版
     * @param context  上下文
     * @return
     */
    @Nonnull
    @Override
    public Mono<Void> send(@Nonnull T template, @Nonnull Map<String,Object> context) {
        return target.send(template, context)
                .switchIfEmpty(Mono.defer(() -> onSuccess(template, context)))
                .onErrorResume(err -> onError(template, context, err).then(Mono.error(err)));
    }

    /**
     * 关闭通知器
     * @return
     */
    @Nonnull
    @Override
    public Mono<Void> close() {
        return target.close();
    }

    @Override
    public <R extends Notifier<T>> R unwrap(Class<R> type) {
        return target.unwrap(type);
    }

    /**
     * 发送失败 错误信息
     * @param template
     * @param ctx
     * @param error
     * @return
     */
    protected Mono<Void> onError(T template, Map<String,Object> ctx, Throwable error) {
        return onEvent(NotifierEvent.builder()
                .cause(error)
                .context(ctx)
                .notifierId(getNotifierId())
                .notifyType(getType())
                .provider(getProvider())
                .template(template)
                .build());
    }

    /**
     * 发送失败错误信息
     * @param templateId
     * @param ctx
     * @param error
     * @return
     */
    protected Mono<Void> onError(String templateId, Map<String,Object> ctx, Throwable error) {
        return onEvent(NotifierEvent.builder()
                .cause(error)
                .context(ctx)
                .notifierId(getNotifierId())
                .notifyType(getType())
                .provider(getProvider())
                .templateId(templateId)
                .build());
    }

    /**
     * 发送成功====
     * @param templateId
     * @param ctx
     * @return
     */
    protected Mono<Void> onSuccess (String templateId,Map<String,Object> ctx){
        return onEvent(NotifierEvent.builder()
                .success(true)
                .context(ctx)
                .notifierId(getNotifierId())
                .notifyType(getType())
                .provider(getProvider())
                .templateId(templateId)
                .build());
    }

    /**
     * 发送成功
     * @param template
     * @param ctx
     * @return
     */
    protected Mono<Void> onSuccess (Template template,Map<String,Object> ctx){
        return onEvent(NotifierEvent.builder()
                .success(true)
                .context(ctx)
                .notifierId(getNotifierId())
                .notifyType(getType())
                .provider(getProvider())
                .template(template)
                .build());
    }

    protected abstract Mono<Void>onEvent(NotifierEvent event);
}
