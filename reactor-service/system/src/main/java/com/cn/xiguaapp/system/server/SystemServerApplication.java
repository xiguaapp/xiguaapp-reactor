package com.cn.xiguaapp.system.server;

import com.cn.xiguaapp.r2dbc.common.core.annotation.EnableEasyormRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import reactivefeign.spring.config.EnableReactiveFeignClients;

/**
 * @author xiguaapp
 * @Date 2020/9/21
 * @desc 系统管理 一切基础数据全靠它了 兄弟
 */
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@EnableDiscoveryClient
@EnableCaching
@EnableReactiveFeignClients(basePackages = "com.cn.xiguaapp.*")
@EnableEasyormRepository("com.cn.**.entity")
public class SystemServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(SystemServerApplication.class);
    }
}
