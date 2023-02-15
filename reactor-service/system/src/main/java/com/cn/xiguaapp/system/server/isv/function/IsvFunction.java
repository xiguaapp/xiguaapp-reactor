package com.cn.xiguaapp.system.server.isv.function;

import com.cn.xiguaapp.r2dbc.common.core.web.reactive.function.ReactiveCrudFunction;
import com.cn.xiguaapp.r2dbc.orm.rdb.mapping.ReactiveRepository;
import com.cn.xiguaapp.system.server.isv.repository.IsvInfoRepository;
import com.cn.xiguaapp.system.api.isv.entity.IsvInfo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author xiguaapp
 * @Date 2020/10/9
 * @desc
 */
@Component
@Slf4j
@AllArgsConstructor
public class IsvFunction implements ReactiveCrudFunction<IsvInfo,Long> {
    @Override
    public ReactiveRepository<IsvInfo, Long> getRepository() {
        return isvInfoRepository.getRepository();
    }
    private final IsvInfoRepository isvInfoRepository;
//    private final IsvKeyRepository isvKeyRepository;
//    private final ConfigPushService configPushService;
//    private final ReactorProperties reactorProperties;
//
//    public static final byte SIGN_TYPE_RSA = 1;
//    public static final byte SIGN_TYPE_MD5 = 2;
//
//
//    static Map<String, Byte> SIGN_TYPE_MAP = new HashMap<>();
//    static {
//        SIGN_TYPE_MAP.put("rsa", (byte) SIGN_TYPE_RSA);
//        SIGN_TYPE_MAP.put("md5", (byte) SIGN_TYPE_MD5);
//    }
//
//    /**
//     * 获取所有isv详情信息
//     * 批注：isvinfo的app_key与isvkey的app_key值相同
//     * @return
//     */
//    @PollableBean
//    public Supplier<Flux<IsvInfo>> listIsvDetail(){
////@Query("SELECT isv_info.app_key appKey ,isv_info.status
//// ,isv_keys.sign_type  signType,isv_keys.secret ,isv_keys.public_key_isv
//// publicKeyIsv ,isv_keys.private_key_platform
//// privateKeyPlatform FROM isv_info,isv_keys
//// WHERE isv_info.app_key = isv_keys.app_key")
//        return isvInfoRepository.createQuery().
//    }
//
//    @Bean
//    public Function<Mono<String>,Mono<IsvKeysVO>>getIsvKeys(){
//        return appkeyMono->appkeyMono.flatMap(appkey->isvKeyRepository
//                .getIsvKeys(appkey)
//                .map(isvKeys -> {
//                    IsvKeysVO isvKeysVO = new IsvKeysVO();
//                    if (Objects.nonNull(isvKeys)){
//                        BeansUtils.copyPropertiesIgnoreNull(isvKeys,isvKeysVO);
//                    }
//                    return isvKeysVO.setAppKey(appkey).setSignType(getSignType());
//                }));
//    }
//
//    private byte getSignType() {
//        return SIGN_TYPE_MAP.getOrDefault(reactorProperties.getSignType(), SIGN_TYPE_RSA);
//    }
//    /**
//     * IsvInfoFormAdd param
//     *     String appKey = new SimpleDateFormat("yyyyMMdd").format(new Date()) + IdGen.nextId();
//     *         IsvInfo rec = new IsvInfo();
//     *         rec.setAppKey(appKey);
//     *         CopyUtil.copyPropertiesIgnoreNull(param, rec);
//     *         isvInfoMapper.saveIgnoreNull(rec);
//     *         if (CollectionUtils.isNotEmpty(param.getRoleCode())) {
//     *             this.saveIsvRole(rec, param.getRoleCode());
//     *         }
//     *         IsvKeysGenVO isvKeysGenVO = this.createIsvKeys();
//     *         IsvKeys isvKeys = new IsvKeys();
//     *         isvKeys.setAppKey(appKey);
//     *         isvKeys.setSignType(getSignType());
//     *         CopyUtil.copyPropertiesIgnoreNull(isvKeysGenVO, isvKeys);
//     *         isvKeysMapper.saveIgnoreNull(isvKeys);
//     *
//     *         this.sendChannelMsg(rec.getAppKey());
//     * @return
//     */
//    /**
//     * 1.生成appkey
//     * 2.赋值给IsvInfo
//     * 3.isvInfo添加
//     * 4.判断权限是否为空
//     *   4.1 不为空则添加
//     * 5.
//     * @return
//     */
//    @Bean
//    @Transactional(rollbackFor = Exception.class)
//    public Consumer<Mono<IsvInfoFormAdd>> addIsv() {
//        return r->{};
//    }
//    //发送渠道信息
//    private Consumer<String>sendChanelMsg(){
//        return appkey->{
//            Mono<IsvDetailDTO> isvDetail = isvInfoRepository.getIsvDetail(appkey);
//            isvDetail.filter(Objects::nonNull)
//                    .subscribe(isv->{
//                        ChannelMsg channelMsg = new ChannelMsg(ChannelOperation.ISV_INFO_UPDATE, JSONObject.parseObject(isv.toString()));
//                        configPushService.publishConfig(NacosConfigs.DATA_ID_ISV, NacosConfigs.GROUP_CHANNEL, channelMsg);
//                    });
//        };
//    }
//
//    private BiConsumer<IsvInfo,Flux<String>>saveIsvRole(){
//        return (IsvInfo,roleCode)->{
//
//        };
//    }
//
//    private IsvKeysGenVO createIsvKeys() throws Exception {
//        IsvKeysGenVO isvFormVO = new IsvKeysGenVO();
//        String secret = IdUtil.randomUUID();
//        isvFormVO.setSecret(secret);
//        RSATool rsaToolIsv = new RSATool(RSATool.KeyFormat.PKCS8,RSATool.KeyLength.LENGTH_2048);
//        RSATool.KeyStore keyStoreIsv = rsaToolIsv.createKeys();
//        isvFormVO.setPublicKeyIsv(keyStoreIsv.getPublicKey());
//        isvFormVO.setPrivateKeyIsv(keyStoreIsv.getPrivateKey());
//
//        isvFormVO.setPublicKeyPlatform("");
//        isvFormVO.setPrivateKeyPlatform("");
//        return isvFormVO;
//    }
////生成公私钥  isv.keys.gen
//    RSATool.KeyStore createPubPriKey(IsvKeysGen param) throws Exception {
//        RSATool.KeyFormat format = RSATool.KeyFormat.PKCS8;
//        Byte keyFormat = param.getKeyFormat();
//        if (keyFormat != null && keyFormat == 2) {
//            format = RSATool.KeyFormat.PKCS1;
//        }
//        RSATool rsaTool = new RSATool(format, RSATool.KeyLength.LENGTH_2048);
//        return rsaTool.createKeys();
//    }
//
////   "isv.secret.gen"
////   description = "生成MD秘钥"
//    String createSecret() throws Exception {
//        return IdUtil.fastUUID();
//    }
//
//    void saveIsvRole(IsvInfo isvInfo, List<String> roleCodeList) {
//
//    }
//
//    /**
//     * 构建ISV拥有的角色
//     *
//     * @param permClient
//     * @return
//     */
//    List<RoleVO> buildIsvRole(IsvInfo permClient) {
////        List<String> roleCodeList = routePermissionService.listClientRoleCode(permClient.getId());
////        if (CollectionUtils.isEmpty(roleCodeList)) {
////            return Collections.emptyList();
////        }
////        List<PermRole> list = permRoleMapper.list(new Query().in("role_code", roleCodeList));
////
////        return list.stream()
////                .map(permRole -> {
////                    RoleVO vo = new RoleVO();
////                    BeansUtils.copyPropertiesIgnoreNull(permRole, vo);
////                    return vo;
////                })
////                .collect(Collectors.toList());
//        return Collections.emptyList();
//    }
//    private IsvInfoVO buildIsvVO(IsvInfo isvInfo) {
//        if (isvInfo == null) {
//            return null;
//        }
//        IsvInfoVO vo = new IsvInfoVO();
//        BeansUtils.copyProperties(isvInfo, vo);
//        vo.setRoleList(this.buildIsvRole(isvInfo));
//        return vo;
//    }

}
