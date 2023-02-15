/*
 *
 *       Copyright (C) <2018-2028>  <@author: xiguaapp @date: 2020/12/8 下午5:00 >
 *
 *       Send: 1125698980@qq.com
 *
 *       This program is free software: you can redistribute it and/or modify
 *       it under the terms of the GNU General Public License as published by
 *       the Free Software Foundation, either version 3 of the License, or
 *       (at your option) any later version.
 *
 *       This program is distributed in the hope that it will be useful,
 *       but WITHOUT ANY WARRANTY; without even the implied warranty of
 *       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *       GNU General Public License for more details.
 *
 *       You should have received a copy of the GNU General Public License
 *       along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package com.cn.xiguaapp.common.gateway.configuration;

import com.alibaba.cloud.nacos.ribbon.NacosServerIntrospector;
import com.cn.xiguaapp.common.gateway.bean.ApiConfig;
import com.cn.xiguaapp.common.gateway.bean.ApiContext;
import com.cn.xiguaapp.common.gateway.bean.BeanInitializer;
import com.cn.xiguaapp.common.gateway.interceptor.RouteInterceptor;
import com.cn.xiguaapp.common.gateway.manage.*;
import com.cn.xiguaapp.common.gateway.message.ErrorFactory;
import com.cn.xiguaapp.common.gateway.param.ParameterFormatter;
import com.cn.xiguaapp.common.gateway.route.RegistryListener;
import com.cn.xiguaapp.common.gateway.route.ServiceListener;
import com.cn.xiguaapp.common.gateway.route.ServiceRouteListener;
import com.cn.xiguaapp.common.gateway.sync.xiguaappAsyncConfigurer;
import com.cn.xiguaapp.common.gateway.utils.RouteInterceptorUtil;
import com.cn.xiguaapp.common.gateway.utils.SpringContext;
import com.cn.xiguaapp.common.gateway.validate.SignConfig;
import com.cn.xiguaapp.common.gateway.validate.Validator;
import com.cn.xiguaapp.system.api.isv.service.IIsvClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.client.discovery.event.HeartbeatEvent;
import org.springframework.cloud.netflix.ribbon.ServerIntrospector;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author xiguaapp
 */
@Slf4j
public class AbstractConfiguration implements ApplicationContextAware, ApplicationRunner {

    private final Lock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();

    private volatile boolean isStartupCompleted;

    @Autowired
    protected Environment environment;

    @Autowired
    private RegistryListener registryListener;

    protected ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext ctx) throws BeansException {
        applicationContext = ctx;
    }

    /**
     * nacos事件监听
     *
     * @param heartbeatEvent
     */
    @EventListener(classes = HeartbeatEvent.class)
    public void listenEvent(ApplicationEvent heartbeatEvent) {
        // 没有启动完毕先等待
//        if (!isStartupCompleted) {
//            lock.lock();
//            try {
//                condition.await();
//            } catch (InterruptedException e) {
//                log.error("condition.await() error", e);
//            } finally {
//                lock.unlock();
//            }
//        }
        registryListener.onEvent(heartbeatEvent);
    }

    @Bean
    @ConditionalOnMissingBean
    ServiceListener serviceListener() {
        return new ServiceRouteListener();
    }

    @Bean
    @ConditionalOnMissingBean
    Validator validator() {
        return ApiConfig.getInstance().getValidator();
    }

    @Bean
    @ConditionalOnMissingBean
    IIsvClient isvManager() {
        return ApiConfig.getInstance().getIsvManager();
    }
//
//    @Bean
//    @ConditionalOnMissingBean
//    IsvRoutePermissionManager isvRoutePermissionManager() {
//        return ApiConfig.getInstance().getIsvRoutePermissionManager();
//    }
//
    @Bean
    @ConditionalOnMissingBean
    RouteConfigManager routeConfigManager() {
        return ApiConfig.getInstance().getRouteConfigManager();
    }
//
//    @Bean
//    @ConditionalOnMissingBean
//    LimitConfigManager limitConfigManager() {
//        return ApiConfig.getInstance().getLimitConfigManager();
//    }
//
//    @Bean
//    @ConditionalOnMissingBean
//    LimitManager limitManager() {
//        return ApiConfig.getInstance().getLimitManager();
//    }
//
//    @Bean
//    @ConditionalOnMissingBean
//    IPBlacklistManager ipBlacklistManager() {
//        return ApiConfig.getInstance().getIpBlacklistManager();
//    }
//
    @Bean
    @ConditionalOnMissingBean
    EnvGrayManager envGrayManager() {
        return ApiConfig.getInstance().getUserKeyManager();
    }
    @Bean
    @ConditionalOnMissingBean
    ParameterFormatter parameterFormatter() {
        return ApiConfig.getInstance().getParameterFormatter();
    }
    @Bean
    public xiguaappAsyncConfigurer sopAsyncConfigurer(@Value("${sop.monitor-route-interceptor.thread-pool-size:4}") int threadPoolSize) {
        return new xiguaappAsyncConfigurer("gatewayAsync", threadPoolSize);
    }

//    @Bean
//    @ConditionalOnMissingBean
//    public MonitorManager monitorManager() {
//        return new MonitorManager();
//    }

    /**
     * 跨域过滤器，gateway采用react形式，需要使用reactive包下的UrlBasedCorsConfigurationSource
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty("sop.gateway-index-path")
    public CorsWebFilter corsWebFilter() {
        org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource source = new org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", createCorsConfiguration());
        return new CorsWebFilter(source);
    }

    private CorsConfiguration createCorsConfiguration() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin("*");
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addAllowedMethod("*");
        return corsConfiguration;
    }

    /**
     * 负责获取nacos实例的metadata
     * @return
     */
    @Bean
    @ConditionalOnProperty("spring.cloud.nacos.discovery.server-addr")
    ServerIntrospector nacosServerIntrospector() {
        return new NacosServerIntrospector();
    }



    @Override
    public void run(ApplicationArguments args) throws Exception {
        this.isStartupCompleted = true;
        lock.lock();
        try {
            condition.signalAll();
        }finally {
            lock.unlock();
        }
        after();
    }

    @PostConstruct
    private void post() {
        EnvironmentContext.setEnvironment(environment);
        SpringContext.setApplicationContext(applicationContext);
    }
//
    public final void after() {
        if (RouteRepositoryContext.getRouteRepository() == null) {
            throw new IllegalArgumentException("RouteRepositoryContext.setRouteRepository()方法未使用");
        }
        String urlencode = EnvironmentKeys.SIGN_URLENCODE.getValue();
        if ("true".equals(urlencode)) {
            SignConfig.enableUrlencodeMode();
        }

        initMessage();
        initBeanInitializer();
        initRouteInterceptor();
        doAfter();
    }

    protected void initBeanInitializer() {
        Map<String, BeanInitializer> beanInitializerMap = applicationContext.getBeansOfType(BeanInitializer.class);
        beanInitializerMap.values().forEach(BeanInitializer::load);
    }

    protected void initRouteInterceptor() {
        Map<String, RouteInterceptor> routeInterceptorMap = applicationContext.getBeansOfType(RouteInterceptor.class);
        Collection<RouteInterceptor> routeInterceptors = new ArrayList<>(routeInterceptorMap.values());
        RouteInterceptorUtil.addInterceptors(routeInterceptors);
    }

    protected void doAfter() {

    }

    protected void initMessage() {
        ErrorFactory.initMessageSource(ApiContext.getApiConfig().getI18nModules());
    }
}
