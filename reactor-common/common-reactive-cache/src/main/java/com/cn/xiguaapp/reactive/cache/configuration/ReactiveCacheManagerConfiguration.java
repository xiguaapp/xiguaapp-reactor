package com.cn.xiguaapp.reactive.cache.configuration;

import com.cn.xiguaapp.reactive.cache.ReactiveCacheManager;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author xiguaapp
 * @desc
 * @since 1.0 17:25
 */
@Configuration
@ConditionalOnMissingBean(ReactiveCacheManager.class)
@EnableConfigurationProperties(ReactiveCacheProperties.class)
public class ReactiveCacheManagerConfiguration {
    @Bean
    public ReactiveCacheManager reactiveCacheManager(ReactiveCacheProperties properties, ApplicationContext context) {

        return properties.createCacheManager(context);

    }
}

