package com.cn.xiguaapp.msg.service.email.embedded;

import com.alibaba.fastjson.JSON;
import com.cn.xiguaapp.msg.service.email.EmailProvider;
import com.cn.xiguaapp.notify.api.constant.DefaultNotifyType;
import com.cn.xiguaapp.notify.api.constant.NotifyType;
import com.cn.xiguaapp.notify.api.core.TemplateProperties;
import com.cn.xiguaapp.notify.api.param.NotifierProperties;
import com.cn.xiguaapp.notify.api.template.*;
import com.cn.xiguapp.common.core.core.config.ConfigMetadata;
import com.cn.xiguapp.common.core.core.config.DefaultConfigMetadata;
import com.cn.xiguapp.common.core.core.config.SimplePropertyMetadata;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import javax.annotation.Nonnull;



@Component
public class DefaultEmailNotifierProvider implements NotifierProvider, TemplateProvider {

    private final TemplateManager templateManager;

    public DefaultEmailNotifierProvider(TemplateManager templateManager) {
        this.templateManager = templateManager;
    }

    @Nonnull
    @Override
    public NotifyType getType() {
        return DefaultNotifyType.email;
    }

    @Nonnull
    @Override
    public Provider getProvider() {
        return EmailProvider.embedded;
    }

    public static final DefaultConfigMetadata templateConfig;

    public static final DefaultConfigMetadata notifierConfig;

    static {
        {
            SimplePropertyMetadata name = new SimplePropertyMetadata();
            name.setId("name");
            name.setName("文件名");

            SimplePropertyMetadata location = new SimplePropertyMetadata();
            location.setId("location");
            location.setName("文件地址");

            templateConfig = new DefaultConfigMetadata("邮件模版", "")
                    .add("subject", "标题", "标题,可使用变量")
                    .add("text", "内容", "")
                    .add("sendTo", "收件人", "")
                    .add("attachments", "附件列表", "");
        }

        {
            SimplePropertyMetadata name = new SimplePropertyMetadata();
            name.setId("name");
            name.setName("配置名称");

            SimplePropertyMetadata value = new SimplePropertyMetadata();
            value.setId("value");
            value.setName("配置值");

            SimplePropertyMetadata description = new SimplePropertyMetadata();
            description.setId("description");
            description.setName("说明");

            notifierConfig = new DefaultConfigMetadata("邮件配置", "")
                    .add("host", "服务器地址", "例如: pop3.qq.com")
                    .add("port", "端口", "")
                    .add("sender", "发件人", "默认和用户名相同")
                    .add("username", "用户名", "")
                    .add("password", "密码", "")
                    .add("properties", "其他配置", "");
        }


    }

    /**
     * 获取邮件配置
     * @return
     */
    @Override
    public ConfigMetadata getNotifierConfigMetadata() {
        return notifierConfig;
    }

    /**
     * 获取邮件模版
     * @return
     */
    @Override
    public ConfigMetadata getTemplateConfigMetadata() {
        return templateConfig;
    }

    @Nonnull
    @Override
    public Mono<DefaultEmailNotifier> createNotifier(@Nonnull NotifierProperties properties) {
        return Mono.fromSupplier(() -> new DefaultEmailNotifier(properties, templateManager));
    }

    @Override
    public Mono<EmailTemplate> createTemplate(TemplateProperties properties) {

        return Mono.fromSupplier(() -> JSON.parseObject(properties.getTemplate(), EmailTemplate.class));
    }
}
