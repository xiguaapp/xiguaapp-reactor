package com.cn.xiguaapp.pay.spring.boot.core.merchant;

import com.cn.xiguaapp.xiguaapp.java.common.api.PayService;

/**
 * @author xiguaapp
 * @desc 支付平台商户列表
 * @since 1.0 23:46
 */
public interface PaymentPlatformMerchantDetails<S extends PayService>  extends MerchantDetails{

    /**
     * 获取支付平台对应的支付服务
     * @return 支付服务
     */
    S getPayService();
    /**
     * 获取支付平台
     * @return 支付平台
     */
    PaymentPlatform getPaymentPlatform();
}
