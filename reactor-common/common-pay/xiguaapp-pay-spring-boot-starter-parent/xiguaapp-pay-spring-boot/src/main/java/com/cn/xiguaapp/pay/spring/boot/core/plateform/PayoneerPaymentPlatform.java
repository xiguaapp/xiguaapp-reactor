package com.cn.xiguaapp.pay.spring.boot.core.plateform;

import com.cn.xiguaapp.xiguaapp.java.common.api.PayConfigStorage;
import com.cn.xiguaapp.xiguaapp.java.common.api.PayService;
import com.cn.xiguaapp.xiguaapp.java.common.core.base.TransactionType;
import com.cn.xiguaapp.xiguaapp.java.common.http.HttpConfigStorage;
import com.cn.xiguaapp.pay.payoneer.api.PayoneerConfigStorage;
import com.cn.xiguaapp.pay.payoneer.api.PayoneerPayService;
import com.cn.xiguaapp.pay.payoneer.constants.PayoneerTransactionType;
import com.cn.xiguaapp.pay.spring.boot.core.merchant.PaymentPlatform;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Configuration;

/**
 * @author xiguaapp
 * @desc P卡(派安盈)支付平台
 * @since 1.0 23:42
 */
@Configuration(PayoneerPaymentPlatform.platformName)
@ConditionalOnMissingBean(PayoneerPaymentPlatform.class)
@ConditionalOnClass(name = {"com.cn.xiguaapp.pay.payoneer.api.PayoneerConfigStorage"})
public class PayoneerPaymentPlatform implements PaymentPlatform {

    public static final String platformName = "payoneerPay";

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
        if (payConfigStorage instanceof PayoneerConfigStorage) {
            return new PayoneerPayService((PayoneerConfigStorage) payConfigStorage);
        }
        PayoneerConfigStorage configStorage = new PayoneerConfigStorage();
        configStorage.setProgramId(payConfigStorage.getPid());
        configStorage.setUserName(payConfigStorage.getSeller());
        configStorage.setApiPassword(payConfigStorage.getKeyPrivate());
        configStorage.setNotifyUrl(payConfigStorage.getNotifyUrl());
        configStorage.setReturnUrl(payConfigStorage.getReturnUrl());
        configStorage.setSignType(payConfigStorage.getSignType());
        configStorage.setPayType(payConfigStorage.getPayType());
//        configStorage.setMsgType(payConfigStorage.getMsgType());
        configStorage.setInputCharset(payConfigStorage.getInputCharset());
        configStorage.setTest(payConfigStorage.isTest());
        return new PayoneerPayService(configStorage);
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
        return PayoneerTransactionType.valueOf(name);
    }


}
