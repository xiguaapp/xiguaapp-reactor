package com.cn.xiguaapp.datasource.api.configuration;

import com.cn.xiguapp.common.core.aop.MethodInterceptorContext;
import com.cn.xiguapp.common.core.aop.MethodInterceptorHolder;
import com.cn.xiguapp.common.core.expression.ExpressionUtils;
import com.cn.xiguaapp.datasource.api.core.DataSourceHolder;
import com.cn.xiguaapp.datasource.api.exception.DataSourceNotFoundException;
import com.cn.xiguaapp.datasource.api.strategy.*;
import org.aopalliance.intercept.MethodInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.StaticMethodMatcherPointcutAdvisor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;


/**
 * 通过aop方式进行对注解方式切换数据源提供支持
 *
 * @author xiguaapp
 * @since 3.0
 */
@Configuration
public class AopDataSourceSwitcherAutoConfiguration {

    @Bean
    @ConfigurationProperties(prefix = "xiguaapp.datasource")
    public ExpressionDataSourceSwitchStrategyMatcher expressionDataSourceSwitchStrategyMatcher() {
        return new ExpressionDataSourceSwitchStrategyMatcher();
    }

    @Bean
    public AnnotationDataSourceSwitchStrategyMatcher annotationDataSourceSwitchStrategyMatcher() {
        return new AnnotationDataSourceSwitchStrategyMatcher();
    }

    @Bean
    public TableSwitchStrategyMatcher alwaysNoMatchStrategyMatcher() {
        return new TableSwitchStrategyMatcher() {
            @Override
            public boolean match(Class target, Method method) {
                return false;
            }

            @Override
            public Strategy getStrategy(MethodInterceptorContext context) {
                return null;
            }
        };
    }

    @Bean
    public SwitcherMethodMatcherPointcutAdvisor switcherMethodMatcherPointcutAdvisor(
            List<DataSourceSwitchStrategyMatcher> matchers,
            List<TableSwitchStrategyMatcher> tableSwitcher) {
        return new SwitcherMethodMatcherPointcutAdvisor(matchers, tableSwitcher);
    }

    public static class SwitcherMethodMatcherPointcutAdvisor extends StaticMethodMatcherPointcutAdvisor {
        private static final Logger logger = LoggerFactory.getLogger(SwitcherMethodMatcherPointcutAdvisor.class);
        private static final long serialVersionUID = 536295121851990398L;

        private List<DataSourceSwitchStrategyMatcher> matchers;

        private List<TableSwitchStrategyMatcher> tableSwitcher;

        private Map<CachedDataSourceSwitchStrategyMatcher.CacheKey, DataSourceSwitchStrategyMatcher> cache
                = new ConcurrentHashMap<>();
        private Map<CachedTableSwitchStrategyMatcher.CacheKey, TableSwitchStrategyMatcher> tableCache
                = new ConcurrentHashMap<>();

        public SwitcherMethodMatcherPointcutAdvisor(List<DataSourceSwitchStrategyMatcher> matchers,
                                                    List<TableSwitchStrategyMatcher> tableSwitcher) {
            this.matchers = matchers;
            this.tableSwitcher = tableSwitcher;
            setAdvice((MethodInterceptor) methodInvocation -> {
                CachedDataSourceSwitchStrategyMatcher.CacheKey key = new CachedDataSourceSwitchStrategyMatcher.CacheKey(ClassUtils.getUserClass(methodInvocation.getThis()), methodInvocation.getMethod());
                CachedTableSwitchStrategyMatcher.CacheKey tableKey = new CachedTableSwitchStrategyMatcher.CacheKey(ClassUtils.getUserClass(methodInvocation.getThis()), methodInvocation.getMethod());

                DataSourceSwitchStrategyMatcher matcher = cache.get(key);
                TableSwitchStrategyMatcher tableMatcher = tableCache.get(tableKey);

                Consumer<MethodInterceptorContext> before = context -> {
                };
                AtomicBoolean dataSourceChanged = new AtomicBoolean(false);
                AtomicBoolean databaseChanged = new AtomicBoolean(false);

                if (matcher != null) {
                    before = before.andThen(context -> {
                        DataSourceSwitchStrategyMatcher.Strategy strategy = matcher.getStrategy(context);
                        if (strategy == null) {
                            dataSourceChanged.set(false);
                            logger.warn("strategy matcher found:{}, but strategy is null!", matcher);
                        } else {
                            logger.debug("switch datasource. use strategy:{}", strategy);
                            if (strategy.isUseDefaultDataSource()) {
                                DataSourceHolder.switcher().datasource().useDefault();
                            } else {
                                try {
                                    String id = strategy.getDataSourceId();
                                    if (StringUtils.hasText(id)) {
                                        if (id.contains("${")) {
                                            id = ExpressionUtils.analytical(id, context.getNamedArguments(),"spel");
                                        }
                                        if (!DataSourceHolder.existing(id)) {
                                            if (strategy.isFallbackDefault()) {
                                                DataSourceHolder.switcher().datasource().useDefault();
                                            } else {
                                                throw new DataSourceNotFoundException("数据源[" + id + "]不存在");
                                            }
                                        } else {
                                            DataSourceHolder.switcher().datasource().use(id);
                                        }
                                        dataSourceChanged.set(true);
                                    }
                                } catch (RuntimeException e) {
                                    dataSourceChanged.set(false);
                                    throw e;
                                } catch (Exception e) {
                                    dataSourceChanged.set(false);
                                    throw new RuntimeException(e.getMessage(), e);
                                }
                            }
                            if (StringUtils.hasText(strategy.getDatabase())) {
                                databaseChanged.set(true);
                                DataSourceHolder.switcher().datasource().use(strategy.getDatabase());
                            }
                        }
                    });
                }
                if (tableMatcher != null) {
                    before = before.andThen(context -> {
                        TableSwitchStrategyMatcher.Strategy strategy = tableMatcher.getStrategy(context);
                        if (null != strategy) {
                            logger.debug("switch table. use strategy:{}", strategy);
                           // strategy.getMapping().forEach(DataSourceHolder.switcher()::use);
                        } else {
                            logger.warn("table strategy matcher found:{}, but strategy is null!", matcher);
                        }
                    });
                }

                Class<?> returnType= methodInvocation.getMethod().getReturnType();

                if(returnType.isAssignableFrom(Flux.class)){
                    // TODO: 2020-12-22
                }
                MethodInterceptorHolder holder = MethodInterceptorHolder.create(methodInvocation);
                before.accept(holder.createParamContext());
                try {
                    return methodInvocation.proceed();
                } finally {
                    if (dataSourceChanged.get()) {
                        DataSourceHolder.switcher().datasource().useLast();
                    }
                    if (databaseChanged.get()) {
                        DataSourceHolder.switcher().datasource().useLast();
                    }
                  //  DataSourceHolder.tableSwitcher().reset();
                }
            });
        }

        @Override
        public boolean matches(Method method, Class<?> aClass) {
            Class<?> targetClass = ClassUtils.getUserClass(aClass);

            CachedDataSourceSwitchStrategyMatcher.CacheKey key = new CachedDataSourceSwitchStrategyMatcher.CacheKey(targetClass, method);
            matchers.stream()
                    .filter(matcher -> matcher.match(targetClass, method))
                    .findFirst()
                    .ifPresent((matcher) -> cache.put(key, matcher));

            boolean datasourceMatched = cache.containsKey(key);
            boolean tableMatched = false;
            if (null != tableSwitcher) {
                CachedTableSwitchStrategyMatcher.CacheKey tableCacheKey = new CachedTableSwitchStrategyMatcher
                        .CacheKey(targetClass, method);
                tableSwitcher.stream()
                        .filter(matcher -> matcher.match(targetClass, method))
                        .findFirst()
                        .ifPresent((matcher) -> tableCache.put(tableCacheKey, matcher));
                tableMatched = tableCache.containsKey(tableCacheKey);
            }

            return datasourceMatched || tableMatched;
        }
    }
}
