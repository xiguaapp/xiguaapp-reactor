package com.cn.xiguaapp.system.api.isv.entity.param;


import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author xiguaapp
 * @Date 2020/10/9
 * @desc isv详细
 */
@Data
@Accessors(chain = true)
public class IsvDetails implements Serializable {
    /** appKey, 数据库字段：app_key */
    private String appKey;

    /** 0启用，1禁用, 数据库字段：status */
    private Integer status;

    /** secret, 数据库字段：secret */
    private String secret;

    /** 开发者生成的公钥, 数据库字段：public_key_isv */
    private String publicKeyIsv;

    /** 平台生成的私钥, 数据库字段：private_key_platform */
    private String privateKeyPlatform;

    /** 签名类型：1:RSA2,2:MD5 */
    private Integer signType;
}
