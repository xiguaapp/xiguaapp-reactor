package com.cn.xiguaapp.system.server.api.service;

import com.cn.xiguaapp.system.api.api.ServiceInfo;
import com.cn.xiguaapp.system.api.api.ServiceInstance;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


/**
 * @author xiguaapp
 * @desc
 * @since 1.0 21:17
 */
@Service
public interface RegistryService {
    String TIMESTAMP_PATTERN = "yyyy-MM-dd HH:mm:ss";

    /**
     * 获取所有服务列表
     *
     * @param pageNo   当前页码
     * @param pageSize 分页大小
     * @return 返回服务列表
     * @throws Exception 获取失败抛出异常
     */
    Flux<ServiceInfo> listAllService(int pageNo, int pageSize) throws Exception;

    /**
     * 服务上线
     *
     * @param serviceInstance
     * @throws Exception 服务上线失败抛出异常
     */
    Mono<Void> onlineInstance(ServiceInstance serviceInstance);

    /**
     * 服务下线
     *
     * @param serviceInstance
     * @throws Exception 服务下线失败抛出异常
     */
    Mono<Void>offlineInstance(ServiceInstance serviceInstance);


    /**
     * 设置实例元数据
     *
     * @param serviceInstance 实例
     * @param key key
     * @param value 值
     * @throws Exception 设置实例元数据失败抛出异常
     */
    Mono<Void> setMetadata(ServiceInstance serviceInstance, String key, String value) throws Exception;
}
