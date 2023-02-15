package com.cn.xiguaapp.msg.service.core.manage;

import com.cn.xiguaapp.notify.api.constant.NotifyType;
import com.cn.xiguaapp.notify.api.param.NotifierProperties;
import com.cn.xiguaapp.notify.api.template.NotifyConfigManager;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import javax.annotation.Nonnull;

/**
 * @author xiguaapp
 * @project-name xiguaapp-reactor
 * @Date 2020/10/27
 * @desc
 */
@Component
public class DefaultNotifyConfigManager implements NotifyConfigManager {
    @Nonnull
    @Override
    public Mono<NotifierProperties> getNotifyConfig(@Nonnull NotifyType notifyType,@Nonnull Long configId) {
        return null;
    }
}
