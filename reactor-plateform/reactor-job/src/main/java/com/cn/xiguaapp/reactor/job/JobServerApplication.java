package com.cn.xiguaapp.reactor.job;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import reactivefeign.spring.config.EnableReactiveFeignClients;

/**
 * @author xiguaapp
 * @Date 2020/9/13
 * @desc 分布式定时任务
 */
@SpringBootApplication
@EnableReactiveFeignClients
@EnableDiscoveryClient
@EnableR2dbcRepositories
public class JobServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(JobServerApplication.class,args);
    }
}
