package com.cn.xiguaapp.notify.api.core;

import com.cn.xiguaapp.notify.api.template.Template;
import lombok.*;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;

/**
 * @author xiguaapp
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SerializableNotifierEvent {
    /**
     * 是否成功
     */
    private boolean success;
    /**
     * 错误类型
     */
    @Nullable
    private String errorType;
    /**
     * 异常信息
     */
    @Nullable
    private String cause;

    @Nonnull
    private Long notifierId;
    /**
     * 通知类型
     */
    @Nonnull
    private String notifyType;
    /**
     * 服务提供商
     */
    @Nonnull
    private String provider;
    /**
     * 模版id
     */
    @Nullable
    private String templateId;
    /**
     * 模版内容
     */
    @Nullable
    private Template template;

    @Nonnull
    private Map<String,Object> context;
}
