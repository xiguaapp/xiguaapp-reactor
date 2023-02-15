package com.cn.xiguaapp.system.server.api.service.impi;

import com.alibaba.nacos.api.PropertyKeyConst;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.cn.xiguaapp.system.api.api.ServiceInfo;
import com.cn.xiguaapp.system.api.api.ServiceInstance;
import com.cn.xiguaapp.system.server.api.service.RegistryService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.util.Properties;

/**
 * @author xiguaapp
 * @desc nacos接口实现
 * @since 1.0 21:21
 */
public class RegistryNacosServiceImpl implements RegistryService {

    @Value("${nacos.discovery.server-addr:${spring.cloud.nacos.discovery.server-addr:}}")
    private String nacosAddr;

    @Value("${nacos.discovery.namespace:${spring.cloud.nacos.discovery.namespace:}}")
    private String nacosNamespace;

    @Value("${nacos.discovery.group:${spring.cloud.nacos.discovery.group:DEFAULT_GROUP}}")
    private String nacosGroup;

    private NamingService namingService;

    @PostConstruct
    public void after() throws NacosException {
        if (StringUtils.isBlank(nacosAddr)) {
            throw new IllegalArgumentException("请在配置文件中指定nacos.discovery.server-addr参数");
        }
        Properties nacosProperties = new Properties();
        nacosProperties.put(PropertyKeyConst.SERVER_ADDR, nacosAddr);
        if (StringUtils.isNotBlank(nacosNamespace)) {
            nacosProperties.put(PropertyKeyConst.NAMESPACE, nacosNamespace);
        }
        namingService = NamingFactory.createNamingService(nacosProperties);
    }
    @Override
    public Flux<ServiceInfo> listAllService(int pageNo, int pageSize) throws Exception {
//        return Flux
//                .fromIterable(namingService
//                        .getServicesOfServer(pageNo,pageSize,nacosGroup)
//                        .getData()).map(serviceName->{
//                            ServiceInfo
//                });
        return null;
    }

    @Override
    public Mono<Void> onlineInstance(ServiceInstance serviceInstance) {
        return null;
    }

    @Override
    public Mono<Void> offlineInstance(ServiceInstance serviceInstance) {
        return null;
    }

    @Override
    public Mono<Void> setMetadata(ServiceInstance serviceInstance, String key, String value) throws Exception {
        return null;
    }
}
