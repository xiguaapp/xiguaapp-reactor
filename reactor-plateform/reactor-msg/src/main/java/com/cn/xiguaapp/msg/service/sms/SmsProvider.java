package com.cn.xiguaapp.msg.service.sms;

import com.cn.xiguaapp.notify.api.template.Provider;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SmsProvider implements Provider {

    aliyunSms("阿里云短信服务")
    ;
    private final String name;

    @Override
    public String getId() {
        return name();
    }

}
