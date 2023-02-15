package com.cn.xiguaapp.pay.spring.boot.core.merchant;

import com.cn.xiguaapp.xiguaapp.java.common.api.PayConfigStorage;
import com.cn.xiguaapp.xiguaapp.java.common.api.PayService;
import com.cn.xiguaapp.xiguaapp.java.common.core.base.BasePayType;
import com.cn.xiguaapp.xiguaapp.java.common.http.HttpConfigStorage;

/**
 * @author xiguaapp
 * @desc 支付平台
 * @since 1.0 23:46
 */
public interface PaymentPlatform<S extends PayService> extends BasePayType {

    /**
     * 获取商户平台
     * @return 商户平台
     */
    String getPlatform();

    /**
     * 获取支付平台对应的支付服务
     * @param payConfigStorage 支付配置
     * @return 支付服务
     */
    S getPayService(PayConfigStorage payConfigStorage);
    /**
     * 获取支付平台对应的支付服务
     * @param payConfigStorage 支付配置
     * @param httpConfigStorage 网络配置
     * @return 支付服务
     */
    S getPayService(PayConfigStorage payConfigStorage, HttpConfigStorage httpConfigStorage);

}
