package com.cn.xiguaapp.notify.api.rule;

import com.cn.xiguaapp.notify.api.constant.DefaultNotifyType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.Assert;

/**
 * @author xiguaapp
 */
@Getter
@Setter
public class RuleNotifierProperties {
    /**
     * 通知渠道
     */
    private DefaultNotifyType notifyType;
    /**
     * 消息通知配置id
     */
    private String notifierId;
    /**
     * 模版id
     */
    private String templateId;

    public void validate() {
        Assert.notNull(notifyType,"通知渠道不能为空");
        Assert.hasText(notifierId,"消息通知配置不能为空");
        Assert.hasText(templateId,"模版id不能为空");

    }
}
