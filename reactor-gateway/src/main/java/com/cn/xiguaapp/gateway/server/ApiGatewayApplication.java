package com.cn.xiguaapp.gateway.server;

import com.cn.xiguaapp.r2dbc.common.core.annotation.EnableEasyormRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import reactivefeign.spring.config.EnableReactiveFeignClients;

/**
 * @author xiguaapp
 * @Date 2020/9/30
 * @desc 网关
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableReactiveFeignClients(basePackages = "com.cn.xiguaapp.*")
@EnableEasyormRepository("com.cn.**.entity")
public class ApiGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class);
    }
}
