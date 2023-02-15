package com.cn.xiguaapp.pay.spring.boot.core.merchant;

import com.cn.xiguaapp.xiguaapp.java.common.api.PayService;
import com.cn.xiguaapp.xiguaapp.java.common.http.HttpConfigStorage;

/**
 * @author xiguaapp
 * @desc 支付商户服务适配器
 * @since 1.0 23:47
 */
public interface PaymentPlatformServiceAdapter<S extends PayService> {

    /**
     * 初始化服务
     * @return 支付商户服务适配器
     */
    PaymentPlatformServiceAdapter initService();

    /**
     * 获取支付平台对应的支付服务
     * @return 支付服务
     */
    S getPayService();



    /**
     * 获取HTTP请求配置
     * @return HTTP请求配置
     */
    HttpConfigStorage getHttpConfigStorage();
}
