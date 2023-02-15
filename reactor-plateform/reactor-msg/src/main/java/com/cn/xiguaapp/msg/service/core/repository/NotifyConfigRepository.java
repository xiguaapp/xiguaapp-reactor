package com.cn.xiguaapp.msg.service.core.repository;

import com.cn.xiguaapp.notify.api.entity.NotifyConfig;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

/**
 * @author xiguaapp
 * @package_name xiguaapp-reactor
 * @Date 十一月
 * @desc
 */
public interface NotifyConfigRepository extends R2dbcRepository<NotifyConfig,Long> {
}
