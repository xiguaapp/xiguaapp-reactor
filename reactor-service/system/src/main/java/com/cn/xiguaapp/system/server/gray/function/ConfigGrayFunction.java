package com.cn.xiguaapp.system.server.gray.function;

import com.cn.xiguaapp.r2dbc.common.core.web.reactive.function.ReactiveCrudFunction;
import com.cn.xiguaapp.r2dbc.orm.rdb.mapping.ReactiveRepository;
import com.cn.xiguaapp.system.api.gray.entity.ConfigGray;
import com.cn.xiguaapp.system.server.gray.repository.ConfigGrayRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.function.Function;

/**
 * @author xiguaapp
 * @desc
 * @since 1.0 01:03
 */
@Component
@Slf4j
@AllArgsConstructor
public class ConfigGrayFunction implements ReactiveCrudFunction<ConfigGray,Long> {
    private final ConfigGrayRepository configGrayRepository;

    public Function<String, Mono<ConfigGray>>findConfigGrayByServiceId(){
        return oo->configGrayRepository.createQuery().is(ConfigGray::getServiceId,oo).fetchOne();
    }

    @Override
    public ReactiveRepository<ConfigGray, Long> getRepository() {
        return configGrayRepository.getRepository();
    }
}
