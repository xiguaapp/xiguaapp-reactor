package com.cn.xiguaapp.msg.service.core.function;

import com.cn.xiguaapp.notify.api.constant.DefaultNotifyType;
import com.cn.xiguaapp.notify.api.entity.NotifyTemplate;
import com.cn.xiguaapp.notify.api.notify.Notifier;
import com.cn.xiguaapp.notify.api.template.NotifierManager;
import com.cn.xiguaapp.notify.api.template.TemplateManager;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * @author xiguaapp
 * @package_name xiguaapp-reactor
 * @Date 十一月
 * @desc 消息通知管理器
 */
@Component
public class NotifierFunction {
    @Autowired
    private NotifierManager notifierManager;
    @Autowired
    private TemplateManager templateManager;

    /**
     * 指定通知器以及模版发送通知
     * 参数1：{@link Notifier#getNotifierId()}  通知器id}
     * 参数2：{@link Mono<SendNotifyRequest> 消息体}
     * @return
     */
    @Bean
    public BiConsumer<Long, Mono<SendNotifyRequest>>sendNotify(){
        return (notifierId,sentNotifyRequest)->sentNotifyRequest.flatMap(tem->{
            DefaultNotifyType type = DefaultNotifyType.valueOf(tem.getTemplate().getType());
            return Mono.zip(
                    notifierManager.getNotifier(type, notifierId).switchIfEmpty(Mono.error(() -> new NullPointerException("通知器[" + notifierId + "]不存在")))
                    , templateManager.createTemplate(type, tem.getTemplate().toTemplateProperties())
                    , (notifier, template) -> notifier.send(template, tem.getContext())).flatMap(Function.identity());
        });
    }


    @Getter
    @Setter
    public static class SendNotifyRequest {
        @NotNull
        //通知模版
        private NotifyTemplate template;

        //上下文数据
        private Map<String, Object> context = new HashMap<>();
    }

}
