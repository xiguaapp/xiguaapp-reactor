package com.cn.xiguaapp.msg.service.core.function;

import com.cn.xiguaapp.msg.service.core.repository.NotifyTemplateRepository;
import com.cn.xiguaapp.notify.api.constant.NotifyType;
import com.cn.xiguaapp.notify.api.entity.NotifyTemplate;
import com.cn.xiguaapp.notify.api.template.Provider;
import com.cn.xiguaapp.notify.api.template.TemplateProvider;
import com.cn.xiguapp.common.core.core.config.ConfigMetadata;
import org.springframework.cloud.function.context.PollableBean;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author xiguaapp
 * @package_name xiguaapp-reactor
 * @Date 十一月
 * @desc 消息通知模版
 */
@Component
public class NotifyTemplateFunction {
    private final NotifyTemplateRepository notifyTemplateRepository;
    private final List<TemplateProvider> providers;

    public NotifyTemplateFunction(NotifyTemplateRepository notifyTemplateRepository, List<TemplateProvider> providers) {
        this.notifyTemplateRepository = notifyTemplateRepository;
        this.providers = providers;
    }

    /**
     * 获取指定类型和服务商所需模板配置定义
     * 参数{参数1:{@link NotifyType#getId()} 参数2:{@link Provider#getId()}} }
     * @return {@link Mono<ConfigMetadata>}
     */
    @Bean
    public BiFunction<String,String, Mono<ConfigMetadata>>getAllTypesNotifyTemplate(){
        return (type,provider)-> Flux.fromIterable(providers)
                .filter(prov->prov.getType().getId().equalsIgnoreCase(type)&&prov.getProvider().getId().equalsIgnoreCase(provider))
                .flatMap(prov->Mono.justOrEmpty(prov.getTemplateConfigMetadata()))
                .next();
    }

    /**
     * 新增模板
     * @return
     */
    @Bean
    public Function<Flux<NotifyTemplate>,Flux<NotifyTemplate>>addNotifyTemplate(){
        return notifyTemplate -> notifyTemplate.flatMap(notifyTemplateRepository::save);
    }

    /**
     * 删除
     * @return
     */
    @Bean
    public Consumer<Flux<Long>>removeByIdNotifyTemplate(){
        return ids->ids.flatMap(notifyTemplateRepository::deleteById);
    }

    /**
     * 查询全部数据
     * @return
     */
    @PollableBean
    public Supplier<Flux<NotifyTemplate>>findAllNotifyTemplate(){
        return notifyTemplateRepository::findAll;
    }
}
