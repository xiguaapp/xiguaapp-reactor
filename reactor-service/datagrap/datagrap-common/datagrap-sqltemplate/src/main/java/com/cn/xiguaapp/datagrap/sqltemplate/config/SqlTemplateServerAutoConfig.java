package com.cn.xiguaapp.datagrap.sqltemplate.config;

import com.cn.xiguapp.common.core.exception.ApiException;
import com.cn.xiguaapp.datagrap.sqltemplate.service.SqlTemplateService;
import com.cn.xiguaapp.datagrap.sqltemplate.service.SqlTemplateServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author xiguaapp
 * @date 2021-02-22
 * @desc sqltemplate配置
 */
@Configuration
@EnableConfigurationProperties(SqlTemplateConfigration.class)
@ConditionalOnClass(SqlTemplateConfigration.class)
@ConditionalOnProperty(prefix = SqlTemplateConfigration.PREFIX,value = "enabled",matchIfMissing = true)
public class SqlTemplateServerAutoConfig {
    @Autowired
    private SqlTemplateConfigration sqlTemplateConfigration;
    @Bean(name = "sqlTemplateService")
    public SqlTemplateService getMyConnectServer() throws ApiException {
        return new SqlTemplateServiceImpl(sqlTemplateConfigration);
    }
}
