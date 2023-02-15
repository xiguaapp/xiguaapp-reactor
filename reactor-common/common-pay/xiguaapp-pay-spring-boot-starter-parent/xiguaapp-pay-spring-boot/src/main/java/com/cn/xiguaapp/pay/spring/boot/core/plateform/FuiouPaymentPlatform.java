package com.cn.xiguaapp.pay.spring.boot.core.plateform;

import com.cn.xiguaapp.pay.fuiou.api.FuiouPayConfigStorage;
import com.cn.xiguaapp.pay.fuiou.api.FuiouPayService;
import com.cn.xiguaapp.pay.fuiou.constants.FuiouTransactionType;
import com.cn.xiguaapp.xiguaapp.java.common.api.PayConfigStorage;
import com.cn.xiguaapp.xiguaapp.java.common.api.PayService;
import com.cn.xiguaapp.xiguaapp.java.common.core.base.TransactionType;
import com.cn.xiguaapp.xiguaapp.java.common.http.HttpConfigStorage;
import com.cn.xiguaapp.pay.spring.boot.core.merchant.PaymentPlatform;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Configuration;

/**
 * @author xiguaapp
 * @desc 富友支付平台
 * @since 1.0 23:41
 */
@Configuration(FuiouPaymentPlatform.platformName)
@ConditionalOnMissingBean(FuiouPaymentPlatform.class)
@ConditionalOnClass(name={"com.cb.xiguaapp.pay.fuiou.api.FuiouPayConfigStorage"})
public class FuiouPaymentPlatform implements PaymentPlatform {
    public static final String platformName = "fuiouPay";





    /**
     * 获取商户平台
     *
     * @return 商户平台
     */
    @Override
    public String getPlatform() {
        return platformName;
    }

    /**
     * 获取支付平台对应的支付服务
     *
     * @param payConfigStorage 支付配置
     * @return 支付服务
     */
    @Override
    public PayService getPayService(PayConfigStorage payConfigStorage) {
        if (payConfigStorage instanceof FuiouPayConfigStorage) {
            return new FuiouPayService((FuiouPayConfigStorage) payConfigStorage);
        }
        FuiouPayConfigStorage configStorage = new FuiouPayConfigStorage();
        configStorage.setMchntCd(payConfigStorage.getPid());
        configStorage.setNotifyUrl(payConfigStorage.getNotifyUrl());
        configStorage.setReturnUrl(payConfigStorage.getReturnUrl());
        configStorage.setSignType(payConfigStorage.getSignType());
        configStorage.setPayType(payConfigStorage.getPayType());
//        configStorage.setMsgType(payConfigStorage.getMsgType());
        configStorage.setInputCharset(payConfigStorage.getInputCharset());
        configStorage.setTest(payConfigStorage.isTest());
        return new FuiouPayService(configStorage);
    }

    /**
     * 获取支付平台对应的支付服务
     *
     * @param payConfigStorage  支付配置
     * @param httpConfigStorage 网络配置
     * @return 支付服务
     */
    @Override
    public PayService getPayService(PayConfigStorage payConfigStorage, HttpConfigStorage httpConfigStorage) {
        PayService payService = getPayService(payConfigStorage);
        payService.setRequestTemplateConfigStorage(httpConfigStorage);
        return payService;
    }

    @Override
    public TransactionType getTransactionType(String name) {
        return FuiouTransactionType.valueOf(name);
    }


}

