package com.cn.xiguaapp.msg.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import reactivefeign.spring.config.EnableReactiveFeignClients;

/**
 * @author xiguaapp
 * @project-name xiguaapp-reactor
 * @Date 2020/10/26
 * @desc 消息服务 叮叮/微信/邮箱/电话/语音/微信服务号通知
 */
@SpringBootApplication
@EnableReactiveFeignClients
@EnableDiscoveryClient
@EnableR2dbcRepositories
public class MsgServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(MsgServerApplication.class);
    }
}
