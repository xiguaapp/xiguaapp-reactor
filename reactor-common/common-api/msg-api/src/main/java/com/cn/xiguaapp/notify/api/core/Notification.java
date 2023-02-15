package com.cn.xiguaapp.notify.api.core;

import com.cn.xiguapp.common.core.utils.SnowflakeIdGenerator;
import com.cn.xiguapp.common.core.utils.BeansUtils;
import com.cn.xiguaapp.notify.api.entity.NotifySubscriber;
import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;

/**
 * @author xiguaapp
 */
@Getter
@Setter
public class Notification implements Serializable {
    private static final long serialVersionUID = -1L;

    private Long id;

    private Long subscribeId;

    private String subscriberType;

    private String subscriber;

    private String topicProvider;

    private String topicName;

    private String message;

    private String dataId;

    private long notifyTime;

    public static Notification from(NotifySubscriber entity) {
        Notification notification = new Notification();

        notification.subscribeId = entity.getId();
        notification.subscriberType = entity.getSubscriberType();
        notification.subscriber = entity.getSubscriber();
        notification.topicName = entity.getTopicName();
        notification.setTopicProvider(entity.getTopicProvider());

        return notification;
    }

    public Notification copyWithMessage(Notify message) {
        Notification target = BeansUtils.copy(this, new Notification());
        target.setId(SnowflakeIdGenerator.getInstance());
        target.setMessage(message.getMessage());
        target.setDataId(message.getDataId());
        target.setNotifyTime(message.getNotifyTime());

        return target;
    }

    public String createTopic() {
        //      /notifications/{订阅者类型:user}/{订阅者ID:userId}/{主题类型}/{订阅ID}
        return "/notifications/" + getSubscriberType() + "/" + getSubscriber() + "/" + getTopicProvider() + "/" + getSubscribeId();
    }
}
