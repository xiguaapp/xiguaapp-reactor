package com.cn.xiguaapp.msg.service.voice.aliyun;

import com.alibaba.fastjson.JSON;
import com.cn.xiguapp.common.core.utils.ValidatorUtils;
import com.cn.xiguaapp.msg.service.voice.VoiceProvider;
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
import reactor.core.publisher.Mono;

import javax.annotation.Nonnull;

/**
 * <a href="https://help.aliyun.com/document_detail/114035.html?spm=a2c4g.11186623.6.561.3d1b3c2dGMXAmk">
 * 阿里云语音通知服务
 * </a>
 *
 * @author xiguaapp
 * @since 1.0
 */
@Slf4j
@AllArgsConstructor
public class AliyunNotifierProvider implements NotifierProvider, TemplateProvider {

    private TemplateManager templateManager;

    @Nonnull
    @Override
    public Provider getProvider() {
        return VoiceProvider.aliyun;
    }

    public static final DefaultConfigMetadata templateConfig = new DefaultConfigMetadata("阿里云语音模版",
            "https://help.aliyun.com/document_detail/114035.html?spm=a2c4g.11186623.6.561.3d1b3c2dGMXAmk")
            .add("ttsCode", "模版ID", "ttsCode")
            .add("calledShowNumbers", "被叫显号", "")
            .add("CalledNumber", "被叫号码", "")
            .add("PlayTimes", "播放次数", "");

    public static final DefaultConfigMetadata notifierConfig = new DefaultConfigMetadata("阿里云通知配置",
            "https://help.aliyun.com/document_detail/114035.html?spm=a2c4g.11186623.6.561.3d1b3c2dGMXAmk")
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
    public Mono<AliyunVoiceTemplate> createTemplate(TemplateProperties properties) {
        return Mono.fromCallable(() -> ValidatorUtils.tryValidate(JSON.parseObject(properties.getTemplate(), AliyunVoiceTemplate.class)));
    }

    @Nonnull
    @Override
    public NotifyType getType() {
        return DefaultNotifyType.voice;
    }

    @Nonnull
    @Override
    public Mono<AliyunVoiceNotifier> createNotifier(@Nonnull NotifierProperties properties) {
        return Mono.fromSupplier(() -> new AliyunVoiceNotifier(properties, templateManager));
    }
}
