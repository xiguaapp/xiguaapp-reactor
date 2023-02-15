package com.cn.xiguaapp.datasource.jta;


import com.cn.xiguaapp.datasource.api.config.DynamicDataSourceConfigRepository;
import com.cn.xiguaapp.datasource.api.configuration.DynamicDataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.jta.atomikos.AtomikosDataSourceBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * @author xiguaapp
 */
@Configuration
@AutoConfigureBefore(DynamicDataSourceAutoConfiguration.class)
public class AtomikosDataSourceAutoConfiguration {

    //默认数据源
    @Bean(initMethod = "init", destroyMethod = "destroy", value = "datasource")
    @Primary
    public AtomikosDataSourceBean datasource() {
        return new AtomikosDataSourceBean();
    }

    @ConditionalOnMissingBean(DynamicDataSourceConfigRepository.class)
    @Bean
    public DynamicDataSourceConfigRepository inMemoryAtomikosDataSourceRepository() {
        return new InMemoryAtomikosDataSourceRepository();
    }



}
