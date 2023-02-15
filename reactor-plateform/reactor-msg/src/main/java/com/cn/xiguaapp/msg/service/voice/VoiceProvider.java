package com.cn.xiguaapp.msg.service.voice;

import com.cn.xiguaapp.notify.api.template.Provider;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum VoiceProvider implements Provider {

    aliyun("阿里云")
    ;

    private String name;

    @Override
    public String getId() {
        return name();
    }
}
