package com.cn.xiguaapp.r2dbc.common.core.entity.factory;

import com.cn.xiguapp.common.core.core.proxy.BeanFactory;
import com.cn.xiguapp.common.core.core.proxy.FastBeanCopier;
import com.cn.xiguapp.common.core.exception.NotFoundException;
import com.cn.xiguapp.common.core.utils.ClassUtils;
import com.cn.xiguaapp.r2dbc.common.api.crud.entity.EntityFactory;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Modifier;
import java.util.*;
import java.util.function.Supplier;

/**
 * @author xiguaapp
 * @desc
 * @since 1.0 15:35
 */
@SuppressWarnings("unchecked")
public class MapperEntityFactory implements EntityFactory, BeanFactory {
    private Map<Class, Mapper> realTypeMapper = new HashMap<>();
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private Map<String, PropertyCopier> copierCache = new HashMap<>();

    private static final DefaultMapperFactory DEFAULT_MAPPER_FACTORY = clazz -> {
        String simpleClassName = clazz.getPackage().getName().concat(".Simple").concat(clazz.getSimpleName());
        try {
            return defaultMapper(org.springframework.util.ClassUtils.forName(simpleClassName,null));
        } catch (ClassNotFoundException ignore) {
            // throw new NotFoundException(e.getMessage());
        }
        return null;
    };

    /**
     * 默认的属性复制器
     */
    private static final DefaultPropertyCopier DEFAULT_PROPERTY_COPIER = FastBeanCopier::copy;

    private DefaultMapperFactory defaultMapperFactory = DEFAULT_MAPPER_FACTORY;

    private DefaultPropertyCopier defaultPropertyCopier = DEFAULT_PROPERTY_COPIER;


    public MapperEntityFactory() {
    }

    public MapperEntityFactory(Map<Class<?>, Mapper> realTypeMapper) {
        this.realTypeMapper.putAll(realTypeMapper);
    }

    public <T> MapperEntityFactory addMapping(Class<T> target, Mapper<? extends T> mapper) {
        realTypeMapper.put(target, mapper);
        return this;
    }

    public MapperEntityFactory addCopier(PropertyCopier copier) {
        Class source = ClassUtils.getGenericType(copier.getClass(), 0);
        Class target = ClassUtils.getGenericType(copier.getClass(), 1);
        if (source == null || source == Object.class) {
            throw new UnsupportedOperationException("generic type " + source + " not support");
        }
        if (target == null || target == Object.class) {
            throw new UnsupportedOperationException("generic type " + target + " not support");
        }
        addCopier(source, target, copier);
        return this;
    }

    public <S, T> MapperEntityFactory addCopier(Class<S> source, Class<T> target, PropertyCopier<S, T> copier) {
        copierCache.put(getCopierCacheKey(source, target), copier);
        return this;
    }

    private String getCopierCacheKey(Class source, Class target) {
        return source.getName().concat("->").concat(target.getName());
    }

    @Override
    public <S, T> T copyProperties(S source, T target) {
        Objects.requireNonNull(source);
        Objects.requireNonNull(target);
        try {
            PropertyCopier<S, T> copier = copierCache.<S, T>get(getCopierCacheKey(source.getClass(), target.getClass()));
            if (null != copier) {
                return copier.copyProperties(source, target);
            }

            return (T) defaultPropertyCopier.copyProperties(source, target);
        } catch (Exception e) {
            logger.warn("copy properties error", e);
        }
        return target;
    }

    protected <T> Mapper<T> initCache(Class<T> beanClass) {
        Mapper<T> mapper = null;
        Class<T> realType = null;
        ServiceLoader<T> serviceLoader = ServiceLoader.load(beanClass, this.getClass().getClassLoader());
        Iterator<T> iterator = serviceLoader.iterator();
        if (iterator.hasNext()) {
            realType = (Class<T>) iterator.next().getClass();
        }

        if (realType == null) {
            mapper = defaultMapperFactory.apply(beanClass);
        }
        if (!Modifier.isInterface(beanClass.getModifiers()) && !Modifier.isAbstract(beanClass.getModifiers())) {
            realType = beanClass;
        }
        if (mapper == null && realType != null) {
            if (logger.isDebugEnabled() && realType != beanClass) {
                logger.debug("use instance {} for {}", realType, beanClass);
            }
            mapper = new Mapper<>(realType, new DefaultInstanceGetter(realType));
        }
        if (mapper != null) {
            realTypeMapper.put(beanClass, mapper);
        }
        return mapper;
    }

    @Override
    public <T> T newInstance(Class<T> beanClass) throws NotFoundException {
        return newInstance(beanClass, null);
    }

    @Override
    public <T> T newInstance(Class<T> beanClass, Class<? extends T> defaultClass) throws NotFoundException {
        if (beanClass == null) {
            return null;
        }
        Mapper<T> mapper = realTypeMapper.get(beanClass);
        if (mapper != null) {
            return mapper.getInstanceGetter().get();
        }
        mapper = initCache(beanClass);
        if (mapper != null) {
            return mapper.getInstanceGetter().get();
        }
        if (defaultClass != null) {
            return newInstance(defaultClass);
        }
        if (Map.class == beanClass) {
            return (T) new HashMap<>();
        }
        if (List.class == beanClass) {
            return (T) new ArrayList<>();
        }
        if (Set.class == beanClass) {
            return (T) new HashSet<>();
        }

        throw new NotFoundException("404","can't create instance for " + beanClass,"20000","isp.unknown-error");
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Class<T> getInstanceType(Class<T> beanClass, boolean autoRegister) {
        if (beanClass == null
                || beanClass.isPrimitive()
                || beanClass.isArray()
                || beanClass.isEnum()) {
            return null;
        }
        Mapper<T> mapper = realTypeMapper.get(beanClass);
        if (null != mapper) {
            return mapper.getTarget();
        }
        if (autoRegister) {
            mapper = initCache(beanClass);
            if (mapper != null) {
                return mapper.getTarget();
            }

            return Modifier.isAbstract(beanClass.getModifiers())
                    || Modifier.isInterface(beanClass.getModifiers())
                    ? null : beanClass;
        }
        return null;
    }

    public void setDefaultMapperFactory(DefaultMapperFactory defaultMapperFactory) {
        Objects.requireNonNull(defaultMapperFactory);
        this.defaultMapperFactory = defaultMapperFactory;
    }

    public void setDefaultPropertyCopier(DefaultPropertyCopier defaultPropertyCopier) {
        this.defaultPropertyCopier = defaultPropertyCopier;
    }

    public static class Mapper<T> {
        Class<T> target;
        Supplier<T> instanceGetter;

        public Mapper(Class<T> target, Supplier<T> instanceGetter) {
            this.target = target;
            this.instanceGetter = instanceGetter;
        }

        public Class<T> getTarget() {
            return target;
        }

        public Supplier<T> getInstanceGetter() {
            return instanceGetter;
        }
    }

    public static <T> Mapper<T> defaultMapper(Class<T> target) {
        return new Mapper<>(target, defaultInstanceGetter(target));
    }

    public static <T> Supplier<T> defaultInstanceGetter(Class<T> clazz) {
        return new DefaultInstanceGetter<>(clazz);
    }

    static class DefaultInstanceGetter<T> implements Supplier<T> {
        Class<T> type;

        public DefaultInstanceGetter(Class<T> type) {
            this.type = type;
        }

        @Override
        @SneakyThrows
        public T get() {
            return type.getDeclaredConstructor().newInstance();
        }
    }
}
