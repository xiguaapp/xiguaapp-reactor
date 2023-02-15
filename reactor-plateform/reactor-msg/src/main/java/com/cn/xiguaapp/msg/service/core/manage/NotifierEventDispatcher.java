package com.cn.xiguaapp.msg.service.core.manage;

import com.cn.xiguapp.common.core.core.event.EventBus;
import com.cn.xiguaapp.notify.api.event.NotifierEvent;
import com.cn.xiguaapp.notify.api.notify.Notifier;
import com.cn.xiguaapp.notify.api.template.NotifierProxy;
import com.cn.xiguaapp.notify.api.template.Template;
import reactor.core.publisher.Mono;

/**
 * @author xiguaapp
 * @project-name xiguaapp-reactor
 * @Date 2020/10/27
 * @desc 消息网关
 */
public class NotifierEventDispatcher<T extends Template> extends NotifierProxy<T> {
    private final EventBus eventBus;

    public NotifierEventDispatcher(EventBus eventBus, Notifier<T> target) {
        super(target);
        this.eventBus = eventBus;
    }

    @Override
    protected Mono<Void> onEvent(NotifierEvent event) {
        // /notify/{notifierId}/success
        return eventBus
                .publish(String.join("/", "/notify", String.valueOf(event.getNotifierId()), event.isSuccess() ? "success" : "error"), event.toSerializable())
                .then();
    }
}
