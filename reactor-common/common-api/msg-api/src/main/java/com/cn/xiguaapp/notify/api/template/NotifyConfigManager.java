package com.cn.xiguaapp.notify.api.template;

import com.cn.xiguaapp.notify.api.constant.DefaultNotifyType;
import com.cn.xiguaapp.notify.api.constant.NotifyType;
import com.cn.xiguaapp.notify.api.param.NotifierProperties;
import reactor.core.publisher.Mono;

import javax.annotation.Nonnull;

/**
 * @author xiguaapp
 * @project-name xiguaapp-reactor
 * @Date 2020/10/27
 * @desc 通知配置管理器 用于统一管理通知配置
 */
public interface NotifyConfigManager {
    /**
     * 根据类型和配置id获取通知器配置
     *  如果配置不存在则返回{@link Mono#empty()},可通过{@link Mono#switchIfEmpty(Mono)}进行处理
     * @param notifyType {@link DefaultNotifyType}
     * @param configId 配置id
     * @return 配置
     */
    Mono<NotifierProperties> getNotifyConfig(@Nonnull NotifyType notifyType,@Nonnull Long configId);
}
