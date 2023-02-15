package com.cn.xiguaapp.r2dbc.common.core.configuration;

import com.cn.xiguaapp.r2dbc.common.api.crud.entity.EntityFactory;
import com.cn.xiguaapp.r2dbc.common.core.annotation.EnableEasyormRepository;
import com.cn.xiguaapp.r2dbc.common.core.entity.factory.MapperEntityFactory;
import com.cn.xiguaapp.r2dbc.common.core.event.CompositeEventListener;
import com.cn.xiguaapp.r2dbc.common.core.event.EntityEventListener;
import com.cn.xiguaapp.r2dbc.common.core.event.ValidateEventListener;
import com.cn.xiguaapp.r2dbc.common.core.generator.CurrentTimeGenerator;
import com.cn.xiguaapp.r2dbc.common.core.generator.DefaultIdGenerator;
import com.cn.xiguaapp.r2dbc.common.core.generator.MD5Generator;
import com.cn.xiguaapp.r2dbc.common.core.generator.SnowFlakeStringIdGenerator;
import com.cn.xiguaapp.r2dbc.orm.feature.Feature;
import com.cn.xiguaapp.r2dbc.orm.rdb.event.EventListener;
import com.cn.xiguaapp.r2dbc.orm.rdb.executor.SyncSqlExecutor;
import com.cn.xiguaapp.r2dbc.orm.rdb.executor.reactive.ReactiveSqlExecutor;
import com.cn.xiguaapp.r2dbc.orm.rdb.mapping.EntityColumnMapping;
import com.cn.xiguaapp.r2dbc.orm.rdb.mapping.EntityManager;
import com.cn.xiguaapp.r2dbc.orm.rdb.mapping.MappingFeatureType;
import com.cn.xiguaapp.r2dbc.orm.rdb.mapping.jpa.JpaEntityTableMetadataParser;
import com.cn.xiguaapp.r2dbc.orm.rdb.mapping.parser.EntityTableMetadataParser;
import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.RDBDatabaseMetadata;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.DatabaseOperator;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.DefaultDatabaseOperator;
import lombok.SneakyThrows;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Optional;

/**
 * @author xiguaapp
 * @desc orm配置类获取
 * @since 1.0 18:02
 */
@Configuration
@EnableConfigurationProperties(EasyormProperties.class)
@EnableEasyormRepository("com.cn.xiguaapp.**.entity")
public class EasyormConfiguration {
    @Autowired
    private EasyormProperties properties;

    static {

    }
    @Bean
    @ConditionalOnMissingBean
    public EntityFactory entityFactory() {
        return new MapperEntityFactory();
    }

    @Bean
    @ConditionalOnMissingBean
    public EntityManager entityManager(EntityTableMetadataResolver resolver, EntityFactory entityFactory) {
        return new EntityManager() {
            @Override
            @SneakyThrows
            public <E> E newInstance(Class<E> type) {
                return entityFactory.newInstance(type);
            }

            @Override
            public EntityColumnMapping getMapping(Class entity) {

                return resolver.resolve(entityFactory.getInstanceType(entity, true))
                        .getFeature(MappingFeatureType.columnPropertyMapping.createFeatureId(entity))
                        .map(EntityColumnMapping.class::cast)
                        .orElse(null);
            }
        };
    }

    @Bean
    public DefaultEntityResultWrapperFactory defaultEntityResultWrapperFactory(EntityManager entityManager) {
        return new DefaultEntityResultWrapperFactory(entityManager);
    }

    @Bean
    @ConditionalOnMissingBean
    public EntityTableMetadataResolver entityTableMappingResolver(List<EntityTableMetadataParser> parsers) {
        CompositeEntityTableMetadataResolver resolver = new CompositeEntityTableMetadataResolver();
        parsers.forEach(resolver::addParser);
        return resolver;
    }

    @Bean
    @ConditionalOnMissingBean
    public EntityTableMetadataParser jpaEntityTableMetadataParser(RDBDatabaseMetadata metadata) {
        JpaEntityTableMetadataParser parser = new JpaEntityTableMetadataParser();
        parser.setDatabaseMetadata(metadata);

        return parser;
    }

    @Bean
    @ConditionalOnMissingBean
    @SuppressWarnings("all")
    public RDBDatabaseMetadata databaseMetadata(Optional<SyncSqlExecutor> syncSqlExecutor,
                                                Optional<ReactiveSqlExecutor> reactiveSqlExecutor) {
        RDBDatabaseMetadata metadata = properties.createDatabaseMetadata();
        syncSqlExecutor.ifPresent(metadata::addFeature);
        reactiveSqlExecutor.ifPresent(metadata::addFeature);

        return metadata;
    }

    @Bean
    @ConditionalOnMissingBean
    public DatabaseOperator databaseOperator(RDBDatabaseMetadata metadata) {

        return DefaultDatabaseOperator.of(metadata);
    }

    @Bean
    public BeanPostProcessor autoRegisterFeature(RDBDatabaseMetadata metadata) {
        CompositeEventListener eventListener = new CompositeEventListener();
        metadata.addFeature(eventListener);
        return new BeanPostProcessor() {
            @Override
            public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {

                if (bean instanceof EventListener) {
                    eventListener.addListener(((EventListener) bean));
                } else if (bean instanceof Feature) {
                    metadata.addFeature(((Feature) bean));
                }

                return bean;
            }
        };
    }

    @Bean
    public EntityEventListener entityEventListener(){
        return new EntityEventListener();
    }

    @Bean
    public ValidateEventListener validateEventListener() {
        return new ValidateEventListener();
    }

    @Bean
    @ConfigurationProperties(prefix = "xiguaapp.default-value-generator")
    public DefaultIdGenerator defaultIdGenerator() {

        return new DefaultIdGenerator();
    }

    @Bean
    public MD5Generator md5Generator() {
        return new MD5Generator();
    }

    @Bean
    public SnowFlakeStringIdGenerator snowFlakeStringIdGenerator() {
        return new SnowFlakeStringIdGenerator();
    }

    @Bean
    public CurrentTimeGenerator currentTimeGenerator() {
        return new CurrentTimeGenerator();
    }

}
