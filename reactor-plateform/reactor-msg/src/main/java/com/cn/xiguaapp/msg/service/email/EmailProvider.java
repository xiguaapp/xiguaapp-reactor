package com.cn.xiguaapp.msg.service.email;

import com.cn.xiguaapp.notify.api.template.Provider;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EmailProvider implements Provider {

    embedded("默认")
    ;

    private String name;

    @Override
    public String getId() {
        return name();
    }
}
