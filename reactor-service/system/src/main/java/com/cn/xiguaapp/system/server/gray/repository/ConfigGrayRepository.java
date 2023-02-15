package com.cn.xiguaapp.system.server.gray.repository;

import com.cn.xiguaapp.r2dbc.common.core.service.GenericReactiveCacheSupportCrudService;
import com.cn.xiguaapp.system.api.gray.entity.ConfigGray;
import org.springframework.stereotype.Service;

/**
 * @author xiguaapp
 * @desc
 * @since 1.0 01:02
 */
@Service
public class ConfigGrayRepository extends GenericReactiveCacheSupportCrudService<ConfigGray, Long> {
}
