package com.cn.xiguaapp.pay.spring.boot.core.merchant;

/**
 * @author xiguaapp
 * @desc 提供给客户端获取商户列表信息的服务
 * @since 1.0 23:45
 */
public interface MerchantDetailsService<T extends MerchantDetails> {

    /**
     *  通过支付商户id加载对应的商户信息列表
     * @param merchantId  支付商户id
     * @return 商户信息列表
     */
    T loadMerchantByMerchantId(String merchantId);

}