package com.cn.xiguaapp.common.gateway.validate;

import com.cn.xiguaapp.common.gateway.bean.ApiParam;

/**
 * @author xiguaapp
 * @Date 2020/10/12
 * @desc
 */
public interface Signer {
    /**
     * 签名校验
     * @param apiParam 参数
     * @param secret 秘钥
     * @return true签名正确
     */
    boolean checkSign(ApiParam apiParam, String secret);
}
