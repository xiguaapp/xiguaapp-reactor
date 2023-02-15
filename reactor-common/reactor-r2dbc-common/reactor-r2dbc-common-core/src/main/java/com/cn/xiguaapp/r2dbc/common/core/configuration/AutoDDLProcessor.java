package com.cn.xiguaapp.r2dbc.common.core.configuration;

import com.cn.xiguaapp.r2dbc.common.api.crud.entity.EntityFactory;
import com.cn.xiguaapp.r2dbc.common.core.entity.factory.MapperEntityFactory;
import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.RDBTableMetadata;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.DatabaseOperator;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author xiguaapp
 * @desc ddl类 实现加载扫描等一系列功能
 * @since 1.0 17:56
 */
@Getter
@Setter
@Slf4j
public class AutoDDLProcessor implements InitializingBean {
    private Set<EntityInfo> entities = new HashSet<>();

    @Autowired
    private DatabaseOperator operator;

    @Autowired
    private EasyormProperties properties;

    @Autowired
    private EntityTableMetadataResolver resolver;

    @Autowired
    private EntityFactory entityFactory;

    private boolean reactive;

    @Override
    @SneakyThrows
    public void afterPropertiesSet() {
        if (entityFactory instanceof MapperEntityFactory) {
            MapperEntityFactory factory = ((MapperEntityFactory) entityFactory);
            for (EntityInfo entity : entities) {
                factory.addMapping(entity.getEntityType(), MapperEntityFactory.defaultMapper(entity.getRealType()));
            }
        }
        List<Class> entities = this.entities.stream().map(EntityInfo::getRealType).collect(Collectors.toList());

        if (properties.isAutoDdl()) {
            operator.getMetadata().getCurrentSchema().loadAllTable();
            //加载全部表信息
//            if (reactive) {
//                Flux.fromIterable(entities)
//                        .doOnNext(type -> log.info("auto ddl for {}", type))
//                        .map(resolver::resolve)
//                        .flatMap(meta->operator.ddl()
//                                .createOrAlter(meta)
//                                .commit()
//                                .reactive())
//                        .doOnError((err) -> log.error(err.getMessage(), err))
//                        .then()
//                        .toFuture().get(2, TimeUnit.MINUTES);
//
//            } else {
            for (Class<?> entity : entities) {
                log.trace("auto ddl for {}", entity);
                try {
                    operator.ddl()
                            .createOrAlter(resolver.resolve(entity))
                            .commit()
                            .sync();
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                    throw e;
                }
            }
//            }
        } else {
            for (Class<?> entity : entities) {
                RDBTableMetadata metadata = resolver.resolve(entity);
                operator.getMetadata()
                        .getCurrentSchema()
                        .addTable(metadata);
            }
        }
    }
}
