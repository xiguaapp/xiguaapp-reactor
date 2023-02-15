package com.cn.xiguaapp.r2dbc.common.core.generator;

import com.cn.xiguaapp.r2dbc.orm.core.DefaultValue;
import com.cn.xiguaapp.r2dbc.orm.core.DefaultValueGenerator;
import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.RDBColumnMetadata;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

/**
 * @author xiguaapp
 * @desc 默认id生成
 * @see DefaultValueGenerator
 * @since 1.0 11:41
 */
@Slf4j
public class DefaultIdGenerator implements DefaultValueGenerator<RDBColumnMetadata> {
    @Getter
    @Setter
    private String defaultId = Generators.SNOW_FLAKE;

    @Getter
    @Setter
    private Map<String, String> mappings = new HashMap<>();

    @Override
    public String getSortId() {
        return Generators.DEFAULT_ID_GENERATOR;
    }

    @Override
    @SneakyThrows
    public DefaultValue generate(RDBColumnMetadata metadata) {
        return Mono.justOrEmpty(mappings.get(metadata.getOwner().getName()))
                .switchIfEmpty(Mono.justOrEmpty(defaultId))
                .flatMap(id->Mono.justOrEmpty(metadata.findFeature(DefaultValueGenerator.createId(id))))
                .doOnNext(gen-> log.debug("use default id generator : {} for column : {}", gen.getSortId(), metadata.getFullName()))
                .map(gen->gen.generate(metadata))
                .switchIfEmpty(Mono.error(()->new UnsupportedOperationException("不支持的生成器:" + defaultId)))
                .toFuture()
                .get();
    }

    @Override
    public String getName() {
        return "默认ID生成器";
    }
}
