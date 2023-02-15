package com.cn.xiguaapp.msg.service.wechat;

import com.alibaba.fastjson.JSON;
import com.cn.xiguapp.common.core.utils.BeansUtils;
import com.cn.xiguapp.common.core.utils.ValidatorUtils;
import com.cn.xiguaapp.notify.api.constant.DefaultNotifyType;
import com.cn.xiguaapp.notify.api.constant.NotifyType;
import com.cn.xiguaapp.notify.api.core.TemplateProperties;
import com.cn.xiguaapp.notify.api.param.NotifierProperties;
import com.cn.xiguaapp.notify.api.template.*;
import com.cn.xiguapp.common.core.core.config.ConfigMetadata;
import com.cn.xiguapp.common.core.core.config.DefaultConfigMetadata;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.annotation.Nonnull;

@Component
public class WechatNotifierProvider implements NotifierProvider, TemplateProvider {

    private WebClient client = WebClient.create();

    private final TemplateManager templateManager;
    public WechatNotifierProvider(TemplateManager templateManager) {
        this.templateManager = templateManager;
    }

    public static final DefaultConfigMetadata notifierConfig = new DefaultConfigMetadata("通知配置", "")
            .add("corpId", "corpId", "")
            .add("corpSecret", "corpSecret", "");

    public static final DefaultConfigMetadata templateConfig = new DefaultConfigMetadata("模版配置", "")
            .add("agentId", "应用ID", "")
            .add("toUser", "收信人ID", "与部门ID不能同时为空")
            .add("toParty", "收信部门ID", "与收信人ID不能同时为空")
            .add("toTag", "按标签推送", "")
            .add("message", "内容", "最大不超过500字");

    @Nonnull
    @Override
    public NotifyType getType() {
        return DefaultNotifyType.weixin;
    }

    @Nonnull
    @Override
    public Provider getProvider() {
        return WechatProvider.corpMessage;
    }

    @Override
    public Mono<WechatMessageTemplate> createTemplate(TemplateProperties properties) {
        return Mono.fromSupplier(() -> ValidatorUtils.tryValidate(JSON.parseObject(properties.getTemplate(), WechatMessageTemplate.class)));
    }

    @Nonnull
    @Override
    public Mono<WeixinCorpNotifier> createNotifier(@Nonnull NotifierProperties properties) {
        return Mono.defer(() -> {
            WechatCorpProperties wechatCorpProperties = BeansUtils.mapToPojo(properties.getConfiguration(),WechatCorpProperties.class);
            return Mono.just(new WeixinCorpNotifier(properties.getId(),client, ValidatorUtils.tryValidate(wechatCorpProperties), templateManager));
        });
    }

    @Override
    public ConfigMetadata getNotifierConfigMetadata() {
        return notifierConfig;
    }

    @Override
    public ConfigMetadata getTemplateConfigMetadata() {
        return templateConfig;
    }
}
