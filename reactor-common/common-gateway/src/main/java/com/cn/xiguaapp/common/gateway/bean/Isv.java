package com.cn.xiguaapp.common.gateway.bean;

/**
 * @author xiguaapp
 * @Date 2020/10/10
 * @desc
 */
public interface Isv {
    /**
     * appKey
     * @return 返回appKey
     */
    String getAppKey();

    /**
     * 秘钥
     * @return 返回秘钥
     */
    String getSecretInfo();

    /**
     * 获取平台的私钥
     * @return 返回私钥
     */
    String getPrivateKeyPlatform();

    /**
     * 0启用，1禁用
     * @return 返回状态
     */
    Integer getStatus();
}
