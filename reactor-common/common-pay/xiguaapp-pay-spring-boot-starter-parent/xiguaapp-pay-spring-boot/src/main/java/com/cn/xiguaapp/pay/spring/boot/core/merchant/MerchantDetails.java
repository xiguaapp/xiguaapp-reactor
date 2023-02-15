package com.cn.xiguaapp.pay.spring.boot.core.merchant;

import com.cn.xiguaapp.xiguaapp.java.common.api.PayConfigStorage;

import java.io.Serializable;

/**
 * @author xiguaapp
 * @desc 商户信息列表
 * @since 1.0 23:45
 */
public interface MerchantDetails extends PayConfigStorage, Serializable {


    /**
     * 获取支付商户详细信息id
     *
     * @return 支付商户详细信息id
     */
    String getDetailsId();
}
