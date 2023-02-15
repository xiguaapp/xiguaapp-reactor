package com.cn.xiguaapp.r2dbc.common.core.configuration;

import com.cn.xiguapp.common.core.utils.ClassUtils;
import com.cn.xiguaapp.r2dbc.common.api.crud.entity.GenericEntity;
import com.cn.xiguaapp.r2dbc.common.api.crud.entity.ImplementFor;
import com.cn.xiguaapp.r2dbc.common.core.annotation.EnableEasyormRepository;
import com.cn.xiguaapp.r2dbc.common.core.annotation.Reactive;
import com.cn.xiguaapp.r2dbc.orm.rdb.mapping.defaults.DefaultReactiveRepository;
import com.cn.xiguaapp.r2dbc.orm.rdb.mapping.defaults.DefaultSyncRepository;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.ResolvableType;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.classreading.SimpleMetadataReaderFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author xiguaapp
 * @desc
 * @since 1.0 17:55
 */
@Slf4j
public class EasyormRepositoryRegistrar implements ImportBeanDefinitionRegistrar {
    private final ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();

    private final MetadataReaderFactory metadataReaderFactory = new SimpleMetadataReaderFactory();

    @Override
    @SneakyThrows
    @SuppressWarnings("all")
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {

        Map<String, Object> attr = importingClassMetadata.getAnnotationAttributes(EnableEasyormRepository.class.getName());
        if (attr == null) {
            return;
        }
        boolean reactivePrecent = org.springframework.util.ClassUtils.isPresent("io.r2dbc.spi.ConnectionFactory", this.getClass().getClassLoader());
        String[] arr = (String[]) attr.get("value");
        String path = Arrays.stream(arr)
                .map(str -> ResourcePatternResolver
                        .CLASSPATH_ALL_URL_PREFIX
                        .concat(str.replace(".", "/")).concat("/**/*.class"))
                .collect(Collectors.joining());

        Class<Annotation>[] anno = (Class[]) attr.get("annotation");

        Set<EntityInfo> entityInfos = new HashSet<>();

        for (Resource resource : resourcePatternResolver.getResources(path)) {
            MetadataReader reader = metadataReaderFactory.getMetadataReader(resource);
            String className = reader.getClassMetadata().getClassName();
            Class<?> entityType = org.springframework.util.ClassUtils.forName(className,null);
            if (Arrays.stream(anno)
                    .noneMatch(ann -> AnnotationUtils.findAnnotation(entityType, ann) != null)) {
                continue;
            }

            ImplementFor implementFor = AnnotationUtils.findAnnotation(entityType, ImplementFor.class);
            Reactive reactive = AnnotationUtils.findAnnotation(entityType, Reactive.class);
            Class genericType = Optional.ofNullable(implementFor)
                    .map(ImplementFor::value)
                    .orElseGet(() -> Stream.of(entityType.getInterfaces())
                                .filter(e -> GenericEntity.class.isAssignableFrom(e))
                                .findFirst()
                                .orElse(entityType));


            Class idType = null;
            if (implementFor == null || implementFor.idType() == Void.class) {
                try {
                    if (GenericEntity.class.isAssignableFrom(entityType)) {
                        idType = ClassUtils.getGenericType(entityType);
                    }
                    if (idType == null) {
                        Method getId = org.springframework.util.ClassUtils.getMethod(entityType, "getId");
                        idType = getId.getReturnType();
                    }
                } catch (Exception e) {
                    idType = String.class;
                }
            } else {
                idType = implementFor.idType();
            }

            EntityInfo entityInfo = new EntityInfo(genericType, entityType, idType, reactivePrecent && (reactive == null || reactive.enable()));
            if (!entityInfos.contains(entityInfo) || implementFor != null) {
                entityInfos.add(entityInfo);
            }

        }
        boolean reactive = false;
        for (EntityInfo entityInfo : entityInfos) {
            Class entityType = entityInfo.getEntityType();
            Class idType = entityInfo.getIdType();
            Class realType = entityInfo.getRealType();
            if (entityInfo.isReactive()) {
                reactive = true;
                log.trace("register ReactiveRepository<{},{}>", entityType.getName(), idType.getSimpleName());

                ResolvableType repositoryType = ResolvableType.forClassWithGenerics(DefaultReactiveRepository.class, entityType, idType);

                RootBeanDefinition definition = new RootBeanDefinition();
                definition.setTargetType(repositoryType);
                definition.setBeanClass(ReactiveRepositoryFactoryBean.class);
                definition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);
                definition.getPropertyValues().add("entityType", realType);
                registry.registerBeanDefinition(realType.getSimpleName().concat("ReactiveRepository"), definition);
            } else {
                log.trace("register SyncRepository<{},{}>", entityType.getName(), idType.getSimpleName());
                ResolvableType repositoryType = ResolvableType.forClassWithGenerics(DefaultSyncRepository.class, entityType, idType);
                RootBeanDefinition definition = new RootBeanDefinition();
                definition.setTargetType(repositoryType);
                definition.setBeanClass(SyncRepositoryFactoryBean.class);
                definition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);
                definition.getPropertyValues().add("entityType", realType);
                registry.registerBeanDefinition(realType.getSimpleName().concat("SyncRepository"), definition);
            }

        }


        try {
            BeanDefinition definition = registry.getBeanDefinition(AutoDDLProcessor.class.getName());
            Set<EntityInfo> infos = (Set) definition.getPropertyValues().get("entities");
            infos.addAll(entityInfos);
        } catch (NoSuchBeanDefinitionException e) {
            RootBeanDefinition definition = new RootBeanDefinition();
            definition.setTargetType(AutoDDLProcessor.class);
            definition.setBeanClass(AutoDDLProcessor.class);
            definition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);
            definition.getPropertyValues().add("entities", entityInfos);
            definition.getPropertyValues().add("reactive", reactive);
            registry.registerBeanDefinition(AutoDDLProcessor.class.getName(), definition);
        }


    }
}
