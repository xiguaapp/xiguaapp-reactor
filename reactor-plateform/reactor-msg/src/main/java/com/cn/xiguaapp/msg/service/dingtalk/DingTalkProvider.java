package com.cn.xiguaapp.msg.service.dingtalk;

import com.cn.xiguaapp.notify.api.template.Provider;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DingTalkProvider implements Provider {
    dingTalkMessage("钉钉消息通知")
    ;

    private String name;

    @Override
    public String getId() {
        return name();
    }

}
