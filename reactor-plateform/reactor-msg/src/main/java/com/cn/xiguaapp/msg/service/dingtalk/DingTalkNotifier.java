package com.cn.xiguaapp.msg.service.dingtalk;

import com.cn.xiguaapp.notify.api.constant.DefaultNotifyType;
import com.cn.xiguaapp.notify.api.constant.NotifyType;
import com.cn.xiguaapp.notify.api.template.AbstractNotifier;
import com.cn.xiguaapp.notify.api.template.Provider;
import com.cn.xiguaapp.notify.api.template.TemplateManager;
import com.cn.ykyoung.server.exception.ServiceException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import javax.annotation.Nonnull;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author xiguaapp
 */
@Slf4j
public class DingTalkNotifier extends AbstractNotifier<DingTalkMessageTemplate> {

    private final AtomicReference<String> accessToken = new AtomicReference<>();

    private long refreshTokenTime;

    private final long tokenTimeOut = Duration.ofSeconds(7000).toMillis();

    private final WebClient client;

    private static final String tokenApi = "https://oapi.dingtalk.com/gettoken";

    private static final String notify = "https://oapi.dingtalk.com/topapi/message/corpconversation/asyncsend_v2";

    private final DingTalkProperties properties;
    @Getter
    private final Long notifierId;

    public DingTalkNotifier(Long id,WebClient client, DingTalkProperties properties, TemplateManager templateManager) {
        super(templateManager);
        this.client = client;
        this.properties = properties;
        this.notifierId = id;
    }

    @Nonnull
    @Override
    public NotifyType getType() {
        return DefaultNotifyType.dingTalk;
    }

    @Nonnull
    @Override
    public Provider getProvider() {
        return DingTalkProvider.dingTalkMessage;
    }

    @Nonnull
    @Override
    public Mono<Void> send(@Nonnull DingTalkMessageTemplate template, @Nonnull Map<String,Object> context) {
        return getToken()
                .flatMap(token ->
                        client.post()
                                .uri(notify)
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .body(template.createFormInserter(BodyInserters.fromFormData("access_token", token),context))
                                .exchange()
                                .flatMap(clientResponse -> clientResponse.bodyToMono(Map.class))
                                .as(as->as.map(param->(Map<String,Object>)param)))
                .then();
    }

    private Mono<Map<String,Object>> checkResult(Mono<Map<String,Object>> msg) {
        return msg.doOnNext(map -> {
            String code = String.valueOf(map.get("errcode"));
            if ("0".equals(code)) {
                log.info("发送钉钉通知成功");
            } else {
                log.warn("发送钉钉通知失败:{}", map);
                throw new ServiceException("发送钉钉通知失败:" + map.get("errmsg"), code);
            }
        });
    }

    private Mono<String> getToken() {
        if (System.currentTimeMillis() - refreshTokenTime > tokenTimeOut || accessToken.get() == null) {
            return requestToken();
        }
        return Mono.just(accessToken.get());
    }

    private Mono<String> requestToken() {
        return client
                .get()
                .uri(UriComponentsBuilder.fromUriString(tokenApi)
                        .queryParam("appkey", properties.getAppKey())
                        .queryParam("appsecret", properties.getAppSecret())
                        .build().toUri())
                .exchange()
                .flatMap(resp -> resp.bodyToMono(HashMap.class))
                .map(map -> {
                    if (map.containsKey("access_token")) {
                        return map.get("access_token");
                    }
                    throw new ServiceException("获取Token失败:" + map.get("errmsg"), String.valueOf(map.get("errcode")));
                })
                .cast(String.class)
                .doOnNext((r) -> {
                    refreshTokenTime = System.currentTimeMillis();
                    accessToken.set(r);
                });
    }

    @Nonnull
    @Override
    public Mono<Void> close() {
        accessToken.set(null);
        refreshTokenTime = 0;
        return Mono.empty();
    }
}