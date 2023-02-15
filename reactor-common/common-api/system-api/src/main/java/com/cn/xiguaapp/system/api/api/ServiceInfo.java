package com.cn.xiguaapp.system.api.api;

import java.util.List;

/**
 * @author xiguaapp
 * @desc 服务信息
 * @since 1.0 11:58
 */
public class ServiceInfo {
    /** 服务名称 */
    private String serviceId;
    /** 实例列表 */
    private List<ServiceInstance> instances;

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public List<ServiceInstance> getInstances() {
        return instances;
    }

    public void setInstances(List<ServiceInstance> instances) {
        this.instances = instances;
    }
}
