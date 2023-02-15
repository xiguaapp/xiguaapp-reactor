package com.cn.xiguaapp.datasource.api.configuration;

import com.cn.xiguaapp.datasource.api.core.DataSourceHolder;
import com.cn.xiguaapp.datasource.api.core.DynamicDataSourceService;
import com.cn.xiguaapp.datasource.api.properties.xiguaappDataSourceProperties;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author xiguaapp
 */
@Configuration
@ImportAutoConfiguration(AopDataSourceSwitcherAutoConfiguration.class)
public class DynamicDataSourceAutoConfiguration {

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    public xiguaappDataSourceProperties xiguaappDataSourceProperties() {
        return new xiguaappDataSourceProperties();
    }

    @Bean
    public BeanPostProcessor switcherInitProcessor() {
        return new BeanPostProcessor() {
            @Override
            public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
                return bean;
            }

            @Override
            public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
                if (bean instanceof DynamicDataSourceService) {
                    DataSourceHolder.dynamicDataSourceService = ((DynamicDataSourceService) bean);
                }

                return bean;
            }
        };
    }


}
