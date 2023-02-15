package com.cn.xiguaapp.msg.service.core.manage;

import com.cn.xiguapp.common.core.core.event.EventBus;
import com.cn.xiguaapp.notify.api.constant.NotifyType;
import com.cn.xiguaapp.notify.api.notify.Notifier;
import com.cn.xiguaapp.notify.api.param.NotifierProperties;
import com.cn.xiguaapp.notify.api.template.NotifierManager;
import com.cn.xiguaapp.notify.api.template.NotifierProvider;
import com.cn.xiguaapp.notify.api.template.NotifyConfigManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author xiguaapp
 * @project-name xiguaapp-reactor
 * @Date 2020/10/27
 * @desc
 */
@Slf4j
@Component
@SuppressWarnings("all")
public class DefaultNotifierManager implements NotifierManager, BeanPostProcessor {
    private final Map<String, Map<String, NotifierProvider>> providers = new ConcurrentHashMap<>();

    private Map<Long, Notifier> notifiers = new ConcurrentHashMap<>();

    private NotifyConfigManager configManager;

    private EventBus eventBus;

    public DefaultNotifierManager(NotifyConfigManager manager, EventBus eventBus) {
        this.configManager = manager;
        this.eventBus = eventBus;
    }

    protected Mono<NotifierProperties> getProperties(NotifyType notifyType,
                                                     Long id) {
        return configManager.getNotifyConfig(notifyType, id);
    }

    /**
     * 重新加载通知管理器
     * @param id 通知管理器ID
     * @return
     */
    @Override
    public Mono<Void> reload(String id) {
        return Mono.justOrEmpty(notifiers.remove(id))
                .flatMap(Notifier::close);
    }

    protected Mono<Notifier> createNotifier(NotifierProperties properties) {
        return Mono.justOrEmpty(providers.get(properties.getType()))
                .switchIfEmpty(Mono.error(new UnsupportedOperationException("不支持的通知类型:" + properties.getType())))
                .flatMap(map -> Mono.justOrEmpty(map.get(properties.getProvider())))
                .switchIfEmpty(Mono.error(new UnsupportedOperationException("不支持的服务商:" + properties.getProvider())))
                .flatMap(notifierProvider -> notifierProvider.createNotifier(properties))
                //转成代理,把通知事件发送到消息网关中.
                .map(notifier -> new NotifierEventDispatcher<>(eventBus, notifier))
                .flatMap(notifier -> Mono.justOrEmpty(notifiers.put(properties.getId(), notifier))
                        .flatMap(Notifier::close)//如果存在旧的通知器则关掉之
                        .onErrorContinue((err, obj) -> log.error((String) err,err))//忽略异常
                        .thenReturn(notifier));
    }

    @Override
    @Nonnull
    public Mono<Notifier> getNotifier(@Nonnull NotifyType type,
                                      @Nonnull Long id) {
        return Mono.justOrEmpty(notifiers.get(id))
                .switchIfEmpty(Mono.defer(() -> getProperties(type, id)).flatMap(this::createNotifier));
    }

    protected void registerProvider(NotifierProvider provider) {
        providers.computeIfAbsent(provider.getType().getId(), ignore -> new ConcurrentHashMap<>())
                .put(provider.getProvider().getId(), provider);
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof NotifierProvider) {
            registerProvider(((NotifierProvider) bean));
        }
        return bean;
    }
}
