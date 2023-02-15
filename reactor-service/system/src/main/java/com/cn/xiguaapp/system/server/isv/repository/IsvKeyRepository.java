package com.cn.xiguaapp.system.server.isv.repository;

import com.cn.xiguaapp.r2dbc.common.core.service.GenericReactiveCacheSupportCrudService;
import com.cn.xiguaapp.system.api.isv.entity.IsvKeys;

/**
 * @author xiguaapp
 * @Date 2020/10/9
 * @desc
 */
public class IsvKeyRepository extends GenericReactiveCacheSupportCrudService<IsvKeys, Long> {
//    @Query("select * from isv_keys where app_key:=appkey")
//    Mono<IsvKeys> getIsvKeys(String appkey);

}
