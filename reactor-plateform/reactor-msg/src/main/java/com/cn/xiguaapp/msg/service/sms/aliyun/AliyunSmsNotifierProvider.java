package com.cn.xiguaapp.msg.service.sms.aliyun;

import com.alibaba.fastjson.JSON;
import com.cn.xiguapp.common.core.utils.ValidatorUtils;
import com.cn.xiguaapp.msg.service.sms.SmsProvider;
import com.cn.xiguaapp.notify.api.constant.DefaultNotifyType;
import com.cn.xiguaapp.notify.api.constant.NotifyType;
import com.cn.xiguaapp.notify.api.core.TemplateProperties;
import com.cn.xiguaapp.notify.api.param.NotifierProperties;
import com.cn.xiguaapp.notify.api.template.NotifierProvider;
import com.cn.xiguaapp.notify.api.template.Provider;
import com.cn.xiguaapp.notify.api.template.TemplateManager;
import com.cn.xiguaapp.notify.api.template.TemplateProvider;
import com.cn.xiguapp.common.core.core.config.ConfigMetadata;
import com.cn.xiguapp.common.core.core.config.DefaultConfigMetadata;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import javax.annotation.Nonnull;

/**
 * 阿里云短信通知服务
 * </a>
 *
 * @author xiguaapp
 * @since 1.3
 */
@Component
@Slf4j
@AllArgsConstructor
public class AliyunSmsNotifierProvider implements NotifierProvider, TemplateProvider {

    private final TemplateManager templateManager;

    @Nonnull
    @Override
    public Provider getProvider() {
        return SmsProvider.aliyunSms;
    }

    public static final DefaultConfigMetadata templateConfig = new DefaultConfigMetadata("阿里云短信模版",
        "https://help.aliyun.com/document_detail/108086.html")
            .add("signName", "签名", "")
            .add("code", "模版编码", "")
            .add("phoneNumber", "收信人", "");

    public static final DefaultConfigMetadata notifierConfig = new DefaultConfigMetadata("阿里云API配置"
        ,"https://help.aliyun.com/document_detail/101300.html")
            .add("regionId", "regionId", "regionId")
            .add("accessKeyId", "accessKeyId", "")
            .add("secret", "secret", "");

    @Override
    public ConfigMetadata getTemplateConfigMetadata() {
        return templateConfig;
    }

    @Override
    public ConfigMetadata getNotifierConfigMetadata() {
        return notifierConfig;
    }

    @Override
    public Mono<AliyunSmsTemplate> createTemplate(TemplateProperties properties) {
        return Mono.fromCallable(() -> ValidatorUtils.tryValidate(JSON.parseObject(properties.getTemplate(), AliyunSmsTemplate.class)));
    }

    @Nonnull
    @Override
    public NotifyType getType() {
        return DefaultNotifyType.sms;
    }

    @Nonnull
    @Override
    public Mono<AliyunSmsNotifier> createNotifier(@Nonnull NotifierProperties properties) {
        return Mono.fromSupplier(() -> new AliyunSmsNotifier(properties, templateManager));
    }
}
