package com.cn.xiguaapp.pay.spring.boot.core.configurers;

import com.cn.xiguaapp.xiguaapp.java.common.api.PayMessageHandler;
import com.cn.xiguaapp.xiguaapp.java.common.api.PayMessageInterceptor;
import com.cn.xiguaapp.xiguaapp.java.common.api.PayService;
import com.cn.xiguaapp.xiguaapp.java.common.core.base.PayMessage;
import com.cn.xiguaapp.pay.spring.boot.core.merchant.PaymentPlatform;

import java.util.List;

/**
 * @author xiguaapp
 * @desc 支付回调通知配置
 * @since 1.0 23:30
 */
public interface PayMessageConfigurer {

    /**
     * 添加处理器
     *
     * @param platform 商户平台，渠道
     * @param handler  处理器
     */
    void addHandler(PaymentPlatform platform, PayMessageHandler handler);

    /**
     * 获取处理器
     *
     * @param platform 商户平台，渠道
     * @return 处理器
     */
    PayMessageHandler<PayMessage, PayService> getHandler(PaymentPlatform platform);

    /**
     * 添加拦截器
     *
     * @param platform    商户平台，渠道
     * @param interceptor 拦截器
     */
    void addInterceptor(PaymentPlatform platform, PayMessageInterceptor interceptor);

    /**
     * 获取拦截器
     *
     * @param platform 商户平台，渠道
     * @return 拦截器
     */
    List<PayMessageInterceptor<PayMessage, PayService>> getInterceptor(PaymentPlatform platform);

}

