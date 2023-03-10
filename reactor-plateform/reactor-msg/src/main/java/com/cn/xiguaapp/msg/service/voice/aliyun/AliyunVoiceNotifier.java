package com.cn.xiguaapp.msg.service.voice.aliyun;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.cn.xiguaapp.msg.service.voice.VoiceProvider;
import com.cn.xiguaapp.notify.api.constant.DefaultNotifyType;
import com.cn.xiguaapp.notify.api.constant.NotifyType;
import com.cn.xiguaapp.notify.api.param.NotifierProperties;
import com.cn.xiguaapp.notify.api.template.AbstractNotifier;
import com.cn.xiguaapp.notify.api.template.Provider;
import com.cn.xiguaapp.notify.api.template.TemplateManager;
import com.cn.ykyoung.server.exception.ServiceException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.Objects;

@Slf4j
public class AliyunVoiceNotifier extends AbstractNotifier<AliyunVoiceTemplate> {

    private final IAcsClient client;
    private String domain = "dyvmsapi.aliyuncs.com";
    private String regionId = "cn-hangzhou";
    private final int connectTimeout = 1000;
    private final int readTimeout = 5000;
    @Getter
    private Long notifierId;

    public AliyunVoiceNotifier(NotifierProperties profile, TemplateManager templateManager) {
        super(templateManager);
        this.notifierId = profile.getId();
        Map<String, Object> config = profile.getConfiguration();
        DefaultProfile defaultProfile = DefaultProfile.getProfile(
                this.regionId = (String) Objects.requireNonNull(config.get("regionId"), "regionId不能为空"),
                (String) Objects.requireNonNull(config.get("accessKeyId"), "accessKeyId不能为空"),
                (String) Objects.requireNonNull(config.get("secret"), "secret不能为空")
        );
        this.client = new DefaultAcsClient(defaultProfile);
        this.domain = (String) config.getOrDefault("domain", "dyvmsapi.aliyuncs.com");
    }

    public AliyunVoiceNotifier(IClientProfile profile, TemplateManager templateManager) {
        this(new DefaultAcsClient(profile), templateManager);
    }

    public AliyunVoiceNotifier(IAcsClient client, TemplateManager templateManager) {
        super(templateManager);
        this.client = client;
    }

    @Override
    @Nonnull
    public NotifyType getType() {
        return DefaultNotifyType.voice;
    }

    @Nonnull
    @Override
    public Provider getProvider() {
        return VoiceProvider.aliyun;
    }

    @Override
    @Nonnull
    public Mono<Void> send(@Nonnull AliyunVoiceTemplate template, @Nonnull Map<String,Object> context) {

        return Mono.<Void>defer(() -> {
            try {
                CommonRequest request = new CommonRequest();
                request.setMethod(MethodType.POST);
                request.setDomain(domain);
                request.setVersion("2017-05-25");
                request.setAction("SingleCallByTts");
                request.setConnectTimeout(connectTimeout);
                request.setReadTimeout(readTimeout);
                request.putQueryParameter("RegionId", regionId);
                request.putQueryParameter("CalledShowNumber", template.getCalledShowNumbers());
                request.putQueryParameter("CalledNumber", template.getCalledNumber());
                request.putQueryParameter("TtsCode", template.getTtsCode());
                request.putQueryParameter("PlayTimes", String.valueOf(template.getPlayTimes()));
                request.putQueryParameter("TtsParam", template.createTtsParam(context));

                CommonResponse response = client.getCommonResponse(request);

                log.info("发起语音通知完成 {}:{}", response.getHttpResponse().getStatus(), response.getData());

                JSONObject json = JSON.parseObject(response.getData());
                if (!"ok".equalsIgnoreCase(json.getString("Code"))) {
                    return Mono.error(new ServiceException(json.getString("Message"), json.getString("Code")));
                }
            } catch (Exception e) {
                return Mono.error(e);
            }
            return Mono.empty();
        }).doOnEach(it->Mono.error(()->new ServiceException("发起语音通话失败")));
    }

    @Override
    @Nonnull
    public Mono<Void> close() {
        return Mono.fromRunnable(client::shutdown);
    }
}
