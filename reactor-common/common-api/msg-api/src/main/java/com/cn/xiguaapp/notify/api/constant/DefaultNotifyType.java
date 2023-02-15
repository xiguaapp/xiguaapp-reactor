package com.cn.xiguaapp.notify.api.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author xiguaapp
 * @desc 通知渠道类型
 */

@Getter
@AllArgsConstructor
public enum DefaultNotifyType implements NotifyType {

    sms("短信"),
    email("邮件"),
    voice("语音"),
    dingTalk("钉钉"),
    weixin("微信"),

    ;

    private String name;

    @Override
    public String getId() {
        return name();
    }
}
