package com.cn.xiguaapp.notify.api.entity;

import com.cn.xiguapp.common.core.utils.BeansUtils;
import com.cn.xiguaapp.common.r2dbc.starter.deprecated.r2dbc.entity.SuperEntity;
import com.cn.xiguaapp.notify.api.constant.NotificationState;
import com.cn.xiguaapp.notify.api.core.Notification;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.relational.core.mapping.Table;

import javax.persistence.Column;
import java.io.Serializable;

/**
 * @author xiguaapp
 * @project-name xiguaapp-reactor
 * @Date 2020/10/26
 * @desc
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(value = "notify_notifications")
@ApiModel(value = "Notifications",description = "消息订阅")
public class Notifications extends SuperEntity implements Serializable {
    private static final long serialVersionUID = -1L;

    @Column(length = 64, nullable = false, updatable = false)
//    @Hidden
    private String subscribeId;

    @Column(length = 32, nullable = false, updatable = false)
//    @Hidden
    private String subscriberType;

    @Column(length = 64, nullable = false, updatable = false)
//    @Hidden
    private String subscriber;

    @Column(length = 32, nullable = false, updatable = false)
//    @Schema(description = "主题标识,如:device_alarm")
    private String topicProvider;

    @Column(length = 64, nullable = false, updatable = false)
//    @Schema(description = "主题名称")
    private String topicName;

    @Column
//    @Schema(description = "通知消息")
    private String message;

    @Column(length = 64)
//    @Schema(description = "数据ID")
    private String dataId;

    @Column(nullable = false)
//    @Schema(description = "通知时间")
    private Long notifyTime;

    @Column(length = 32)
//    @Schema(description = "通知状态")
    private NotificationState state;

    @Column(length = 1024)
//    @Schema(description = "说明")
    private String description;

    public static Notifications from(Notification notification) {
        Notifications notifications = new Notifications();
        BeansUtils.copyPropertiesIgnoreNull(notification,notifications);
        return notifications;
    }
}
