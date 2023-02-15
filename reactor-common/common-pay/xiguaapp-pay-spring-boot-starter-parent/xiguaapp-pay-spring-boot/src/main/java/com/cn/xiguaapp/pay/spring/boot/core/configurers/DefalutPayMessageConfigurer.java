package com.cn.xiguaapp.pay.spring.boot.core.configurers;

import com.cn.xiguaapp.xiguaapp.java.common.api.PayMessageHandler;
import com.cn.xiguaapp.xiguaapp.java.common.api.PayMessageInterceptor;
import com.cn.xiguaapp.xiguaapp.java.common.api.PayService;
import com.cn.xiguaapp.xiguaapp.java.common.core.base.PayMessage;
import com.cn.xiguaapp.pay.spring.boot.core.merchant.PaymentPlatform;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xiguaapp
 * @desc 支付回调配置中心
 * @since 1.0 23:30
 */
public class DefalutPayMessageConfigurer implements PayMessageConfigurer {

    /**
     * 消息处理器集
     *
     * key 为商户平台
     * value 为对应得处理器
     */
    private Map<PaymentPlatform, PayMessageHandler<PayMessage, PayService>> handlers = new HashMap<PaymentPlatform, PayMessageHandler<PayMessage, PayService>>();

    /**
     * 消息拦截器集
     *
     * key 为商户平台
     * value 为对应得拦截器
     */
    private Map<PaymentPlatform, List<PayMessageInterceptor<PayMessage, PayService>>> interceptors = new HashMap<PaymentPlatform, List<PayMessageInterceptor<PayMessage, PayService>>>();

    /**
     * 添加处理器
     *
     * @param platform 商户平台，渠道
     * @param handler  处理器
     */
    public void addHandler(PaymentPlatform platform, PayMessageHandler handler) {
        if (handlers.containsKey(platform)){
            throw new IllegalArgumentException(platform.getPlatform() + "已存在，请勿重复设置");
        }
        this.handlers.put(platform, handler);
    }

    /**
     * 获取处理器
     *
     * @param platform 商户平台，渠道
     * @return 处理器
     */
    @Override
    public PayMessageHandler<PayMessage, PayService> getHandler(PaymentPlatform platform) {
        return handlers.get(platform);
    }
    /**
     * 添加拦截器
     *
     * @param platform    商户平台，渠道
     * @param interceptor 拦截器
     */
    @Override
    public void addInterceptor(PaymentPlatform platform, PayMessageInterceptor interceptor) {
        List<PayMessageInterceptor<PayMessage, PayService>> interceptors = this.interceptors.get(platform);
        if (null == interceptors){
            interceptors = new ArrayList<PayMessageInterceptor<PayMessage, PayService>>();
            this.interceptors.put(platform, interceptors);
        }
        interceptors.add(interceptor);
    }

    /**
     * 获取拦截器
     *
     * @param platform 商户平台，渠道
     * @return 拦截器
     */
    @Override
    public List<PayMessageInterceptor<PayMessage, PayService>> getInterceptor(PaymentPlatform platform) {
        return interceptors.get(platform);
    }
}
