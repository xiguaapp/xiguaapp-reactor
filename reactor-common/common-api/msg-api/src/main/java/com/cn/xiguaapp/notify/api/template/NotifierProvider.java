package com.cn.xiguaapp.notify.api.template;

import com.cn.xiguaapp.notify.api.constant.DefaultNotifyType;
import com.cn.xiguaapp.notify.api.constant.NotifyType;
import com.cn.xiguaapp.notify.api.notify.Notifier;
import com.cn.xiguaapp.notify.api.param.NotifierProperties;
import com.cn.xiguapp.common.core.core.config.ConfigMetadata;
import reactor.core.publisher.Mono;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author xiguaapp
 * @project-name xiguaapp-reactor
 * @Date 2020/10/27
 * @desc 通知服务提供商
 */
public interface NotifierProvider {
    /**
     * 获取通知类型
     *
     * @return 通知类型
     * @see DefaultNotifyType
     */
    @Nonnull
    NotifyType getType();

    /**
     * @return 服务商
     */
    @Nonnull
    Provider getProvider();

    /**
     * 根据配置创建通知器
     *
     * @param properties 通知配置
     * @return 创建结果
     */
    @Nonnull
    Mono<? extends Notifier<? extends Template>> createNotifier(@Nonnull NotifierProperties properties);

    /**
     * 获取通知配置元数据,通过元数据可以知道此通知所需要的配置信息
     *
     * @return 配置元数据
     */
    @Nullable
    default ConfigMetadata getNotifierConfigMetadata() {
        return null;
    }
}
