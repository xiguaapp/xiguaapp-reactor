package com.cn.xiguaapp.msg.service.wechat;

import com.cn.xiguaapp.notify.api.template.Provider;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum WechatProvider implements Provider {
    corpMessage("微信企业消息通知")
    ;

    private String name;

    @Override
    public String getId() {
        return name();
    }

}
