package com.cn.xiguaapp.msg.service.sms.aliyun;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.cn.xiguaapp.msg.service.sms.SmsProvider;
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
import reactor.core.scheduler.Schedulers;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.Objects;

@Slf4j
public class AliyunSmsNotifier extends AbstractNotifier<AliyunSmsTemplate> {

    private final IAcsClient client;
    private final int connectTimeout = 1000;
    private final int readTimeout = 5000;

    @Getter
    private Long notifierId;

    private String domain = "dysmsapi.aliyuncs.com";
    private String regionId = "cn-hangzhou";

    public AliyunSmsNotifier(NotifierProperties profile, TemplateManager templateManager) {
        super(templateManager);
        Map<String, Object> config = profile.getConfiguration();
        DefaultProfile defaultProfile = DefaultProfile.getProfile(
            this.regionId = (String) Objects.requireNonNull(config.get("regionId"), "regionId不能为空"),
            (String) Objects.requireNonNull(config.get("accessKeyId"), "accessKeyId不能为空"),
            (String) Objects.requireNonNull(config.get("secret"), "secret不能为空")
        );
        this.client = new DefaultAcsClient(defaultProfile);
        this.domain = (String) config.getOrDefault("domain", domain);
        this.notifierId = profile.getId();
    }

    public AliyunSmsNotifier(IClientProfile profile, TemplateManager templateManager) {
        this(new DefaultAcsClient(profile), templateManager);
    }

    public AliyunSmsNotifier(IAcsClient client, TemplateManager templateManager) {
        super(templateManager);
        this.client = client;
    }

    @Override
    @Nonnull
    public NotifyType getType() {
        return DefaultNotifyType.sms;
    }

    @Nonnull
    @Override
    public Provider getProvider() {
        return SmsProvider.aliyunSms;
    }

    @Override
    @Nonnull
    public Mono<Void> send(@Nonnull AliyunSmsTemplate template, @Nonnull Map<String,Object> context) {

        return Mono.<Void>defer(() -> {
            try {
                CommonRequest request = new CommonRequest();
                request.setSysMethod(MethodType.POST);
                request.setSysDomain(domain);
                request.setSysVersion("2017-05-25");
                request.setSysAction("SendSms");
                request.setSysConnectTimeout(connectTimeout);
                request.setSysReadTimeout(readTimeout);
                request.putQueryParameter("RegionId", regionId);
                request.putQueryParameter("PhoneNumbers", template.getPhoneNumber());
                request.putQueryParameter("SignName", template.getSignName());
                request.putQueryParameter("TemplateCode", template.getCode());
                request.putQueryParameter("TemplateParam", template.createTtsParam(context));

                CommonResponse response = client.getCommonResponse(request);

                log.info("发送短信通知完成 {}:{}", response.getHttpResponse().getStatus(), response.getData());

                JSONObject json = JSON.parseObject(response.getData());
                if (!"ok".equalsIgnoreCase(json.getString("Code"))) {
                    return Mono.error(new ServiceException(json.getString("Message"), json.getString("Code")));
                }
            } catch (Exception e) {
                return Mono.error(e);
            }
            return Mono.empty();
        }).doOnEach(it->Mono.error(()->new ServiceException("发送短信通知失败"))
        ).subscribeOn(Schedulers.elastic());
    }

    @Override
    @Nonnull
    public Mono<Void> close() {
        return Mono.fromRunnable(client::shutdown);
    }
}
