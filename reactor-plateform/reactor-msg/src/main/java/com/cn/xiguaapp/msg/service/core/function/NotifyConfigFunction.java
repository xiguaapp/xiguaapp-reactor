package com.cn.xiguaapp.msg.service.core.function;

import com.cn.xiguaapp.msg.service.core.repository.NotifyConfigRepository;
import com.cn.xiguaapp.notify.api.constant.NotifyType;
import com.cn.xiguaapp.notify.api.entity.NotifyConfig;
import com.cn.xiguaapp.notify.api.template.NotifierProvider;
import com.cn.xiguaapp.notify.api.template.Provider;
import com.cn.xiguapp.common.core.core.config.ConfigMetadata;
import lombok.*;
import org.springframework.cloud.function.context.PollableBean;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * @author xiguaapp
 * @package_name xiguaapp-reactor
 * @Date 十一月
 * @desc 消息通知配置
 */
@Component
public class NotifyConfigFunction {
    private final NotifyConfigRepository notifyConfigRepository;
    private final List<NotifierProvider> providers;

    public NotifyConfigFunction(NotifyConfigRepository notifyConfigRepository, List<NotifierProvider> providers) {
        this.notifyConfigRepository = notifyConfigRepository;
        this.providers = providers;
    }


    /**
     * 获取指定类型和服务商所需配置定义
     * @see BiFunction 参数传输详情查看 {@link NotifyType#getId()} {@link Provider#getId()}
     * @return Mono<ConfigMetadata> 参数返回查看 {@link ConfigMetadata}
     */
    @Bean
    public BiFunction<String,String, Mono<ConfigMetadata>>getAllTypesNotifyConfigConfigMetadata(){
        return (type,provider)-> Flux
                .fromIterable(providers)
                .filter(prov->prov.getType().getId().equalsIgnoreCase(type)
                        &&prov.getProvider().getId().equalsIgnoreCase(provider))
                .flatMap(prov->Mono.justOrEmpty(prov.getNotifierConfigMetadata()))
                .next();
    }

    /**
     * 获取平台通知类型
     *
     * @return {@link Flux<NotifyTypeInfo>}
     */
    @PollableBean
    public Supplier<Flux<NotifyTypeInfo>>getAllTypesNotifyConfig(){
        return ()->Flux.fromIterable(providers)
                .collect(Collectors.groupingBy(NotifierProvider::getType))
                .flatMapIterable(Map::entrySet)
                .map(sh-> NotifyTypeInfo
                        .builder()
                        .id(sh.getKey().getId())
                        .name(sh.getKey().getName())
                        .providerInfos(sh.getValue()
                                .stream()
                                .map(ProviderInfo::of)
                                .collect(Collectors.toList()))
                        .build());
    };

    /**
     * 获取支持的服务商
     * @see NotifyType#getId() 参数详情
     * @return {@link ProviderInfo#of(NotifierProvider)}
     */
    @Bean
    public Function<String,Flux<ProviderInfo>>getTypeProvidersNotifyConfig(){
        return type->Flux.fromIterable(providers)
                .filter(provider->provider.getType().getId().equals(type))
                .map(ProviderInfo::of);
    }
    @Bean
    public Function<Flux<NotifyConfig>,Flux<NotifyConfig>>insertNotifyConfig(){
        return flux->flux.flatMap(notifyConfigRepository::save);
    }
    @Bean
    public Consumer<Flux<Long>>removeByIdNotifyConfig(){
        return ids->ids.flatMap(notifyConfigRepository::deleteById);
    }

    @Getter
    @Setter
    @EqualsAndHashCode(of = "id")
    @Builder
    public static class NotifyTypeInfo {
//      通知类型ID
        private String id;

//      通知类型名称
        private String name;

//      服务商信息
        private List<ProviderInfo> providerInfos;

    }

    @AllArgsConstructor
    @Getter
    @Builder
    public static class ProviderInfo {

//      通知类型
        private final String type;

//      服务商ID
        private final String id;

//      服务商名称
        private final String name;

        public static ProviderInfo of(NotifierProvider provider) {
            return ProviderInfo
                    .builder()
                    .type(provider.getType().getId())
                    .id(provider.getProvider().getId())
                    .name(provider.getProvider().getName())
                    .build();
        }

    }

}
