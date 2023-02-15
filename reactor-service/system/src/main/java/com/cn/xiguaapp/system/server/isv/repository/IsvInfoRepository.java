package com.cn.xiguaapp.system.server.isv.repository;

import com.cn.xiguaapp.r2dbc.common.core.service.GenericReactiveCacheSupportCrudService;
import com.cn.xiguaapp.system.api.isv.entity.IsvInfo;
import org.springframework.stereotype.Service;

/**
 * @author xiguaapp
 * @Date 2020/10/9
 * @desc
 */
@Service
public class IsvInfoRepository extends GenericReactiveCacheSupportCrudService<IsvInfo,Long> {
//    @Query("SELECT isv_info.app_key appKey ,isv_info.status ,isv_keys.sign_type  signType,isv_keys.secret ,isv_keys.public_key_isv  publicKeyIsv ,isv_keys.private_key_platform  privateKeyPlatform FROM isv_info,isv_keys WHERE isv_info.app_key = isv_keys.app_key")
//    Flux<IsvInfo> listIsvDetail();
//    @Query("SELECT t.app_key appKey,t.STATUS,t2.sign_type signType,t2.secret,t2.public_key_isv publicKeyIsv,t2.private_key_platform privateKeyPlatform FROM isv_info t INNER JOIN isv_keys t2 ON t.app_key = t2.app_key WHERE t.app_key := appKey")
//    Mono<IsvDetailDTO> getIsvDetail(String appKey);
}
