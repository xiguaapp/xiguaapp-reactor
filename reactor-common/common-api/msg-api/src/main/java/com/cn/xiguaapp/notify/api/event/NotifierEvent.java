package com.cn.xiguaapp.notify.api.event;

import com.cn.xiguapp.common.core.utils.StringUtils;
import com.cn.xiguaapp.notify.api.constant.NotifyType;
import com.cn.xiguaapp.notify.api.core.SerializableNotifierEvent;
import com.cn.xiguaapp.notify.api.template.Provider;
import com.cn.xiguaapp.notify.api.template.Template;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;

/**
 * @author xiguaapp
 */
@Getter
@Setter
@Builder
public class NotifierEvent {
    /**
     * 状态
     */
    private boolean success;
    /**
     * 异常
     */
    @Nullable
    private Throwable cause;

    @Nonnull
    private Long notifierId;
    /**
     * 通知类型
     */
    @Nonnull
    private NotifyType notifyType;
    /**
     * 服务提供商
     */
    @Nonnull
    private Provider provider;
    /**
     * 模版id
     */
    @Nullable
    private String templateId;
    /**
     * 通知模版
     */
    @Nullable
    private Template template;

    @Nonnull
    private Map<String, Object> context;

    public SerializableNotifierEvent toSerializable() {
        return SerializableNotifierEvent.builder()
            .success(success)
            .notifierId(notifierId)
            .notifyType(notifyType.getId())
            .provider(provider.getId())
            .templateId(templateId)
            .template(template)
            .context(context)
            .cause(cause != null ? StringUtils.throwable2String(cause) : "")
            .errorType(cause != null ? cause.getClass().getName() : null)
            .build();
    }
}
