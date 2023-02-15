package com.cn.xiguaapp.msg.service.sms;

import com.cn.xiguaapp.notify.api.template.Template;
import com.cn.xiguapp.common.core.core.config.DefaultConfigMetadata;
import com.cn.xiguapp.common.core.expression.ExpressionUtils;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class PlainTextSmsTemplate implements Template {

    public static final DefaultConfigMetadata templateConfig = new DefaultConfigMetadata("模版配置", "")
            .add("text", "短信内容", "短信内容,支持使用变量:${ }")
            .add("sendTo", "收件人", "");

    private String text;

    private List<String> sendTo;

    public String getTextSms(Map<String, Object> context) {
        return ExpressionUtils.analytical(text, context,"spel");
    }

    public String[] getSendTo(Map<String, Object> context) {

        return sendTo.stream()
                .map(str -> ExpressionUtils.analytical(str, context,"spel"))
                .toArray(String[]::new);

    }

}
