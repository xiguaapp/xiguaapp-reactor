package com.cn.xiguaapp.notify.api.template;

import com.cn.xiguaapp.notify.api.notify.Notifier;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

import javax.annotation.Nonnull;
import java.util.Map;

/**
 * @author xiguaapp
 * @desc 消息发送
 */
@AllArgsConstructor
public abstract class AbstractNotifier<T extends Template> implements Notifier<T> {

    private final TemplateManager templateManager;

    @Override
    @Nonnull
    public Mono<Void> send(@Nonnull String templateId, @Nonnull Map<String,Object> context) {
        return templateManager
                .getTemplate(getType(), templateId) //通过通知类型和模版ID获取模版
                .switchIfEmpty(Mono.error(new UnsupportedOperationException("模版不存在:" + templateId)))
                .flatMap(tem -> send((T) tem, context));
    }


}
