package com.cn.xiguaapp.pay.spring.boot.core.plateform;

import com.cn.xiguaapp.xiguaapp.java.common.api.PayConfigStorage;
import com.cn.xiguaapp.xiguaapp.java.common.api.PayService;
import com.cn.xiguaapp.xiguaapp.java.common.core.base.CertStoreType;
import com.cn.xiguaapp.xiguaapp.java.common.core.base.TransactionType;
import com.cn.xiguaapp.xiguaapp.java.common.http.HttpConfigStorage;
import com.cn.xiguaapp.pay.spring.boot.core.merchant.PaymentPlatform;
import com.cn.xiguaapp.pay.spring.boot.core.merchant.info.CommonPaymentPlatformMerchantDetails;
import com.cn.xiguaapp.pay.wx.api.WxPayConfigStorage;
import com.cn.xiguaapp.pay.wx.api.WxPayService;
import com.cn.xiguaapp.pay.wx.constants.WxTransactionType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

/**
 * @author xiguaapp
 * @desc 微信支付平台
 * @since 1.0 23:44
 */
@Configuration(WxPaymentPlatform.platformName)
@ConditionalOnMissingBean(WxPaymentPlatform.class)
//@ConditionalOnClass(name = {"com.cn.xiguaapp.pay.wx.api.WxPayConfigStorage"})
@ConditionalOnClass(value = WxPayConfigStorage.class)
public class WxPaymentPlatform extends WxPayConfigStorage implements PaymentPlatform {

    protected final Log LOG = LogFactory.getLog(WxPaymentPlatform.class);

    public static final String platformName = "wxPay";



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
        return getPayService(payConfigStorage, null);
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
        if (payConfigStorage instanceof WxPayConfigStorage) {
            WxPayService wxPayService = new WxPayService((WxPayConfigStorage) payConfigStorage);
            wxPayService.setRequestTemplateConfigStorage(httpConfigStorage);
            return wxPayService;
        }
        WxPayConfigStorage configStorage = new WxPayConfigStorage();
        configStorage.setInputCharset(payConfigStorage.getInputCharset());
        configStorage.setAppid(payConfigStorage.getAppid());
        configStorage.setMchId(payConfigStorage.getPid());
        configStorage.setAttach(payConfigStorage.getAttach());
        configStorage.setKeyPrivate(payConfigStorage.getKeyPrivate());
        configStorage.setKeyPublic(payConfigStorage.getKeyPublic());
        configStorage.setNotifyUrl(payConfigStorage.getNotifyUrl());
        configStorage.setReturnUrl(payConfigStorage.getReturnUrl());
//        configStorage.setMsgType(payConfigStorage.getMsgType());
        configStorage.setPayType(payConfigStorage.getPayType());
        configStorage.setTest(payConfigStorage.isTest());
        configStorage.setSignType(payConfigStorage.getSignType());

        if (payConfigStorage instanceof CommonPaymentPlatformMerchantDetails) {
            CommonPaymentPlatformMerchantDetails merchantDetails = (CommonPaymentPlatformMerchantDetails) payConfigStorage;
            configStorage.setSubAppid(merchantDetails.getSubAppId());
            configStorage.setSubMchId(merchantDetails.getSubMchId());
            if (null != merchantDetails.getKeyCert()) {
                if (null == httpConfigStorage) {
                    httpConfigStorage = new HttpConfigStorage();
                }
                httpConfigStorage.setCertStoreType(merchantDetails.getCertStoreType());
                try {
                    httpConfigStorage.setKeystore(merchantDetails.getKeyCertInputStream());
                } catch (IOException e) {
                    LOG.error(e);
                }

                httpConfigStorage.setCertStoreType(CertStoreType.INPUT_STREAM);
                httpConfigStorage.setStorePassword(merchantDetails.getKeystorePwd());

            }
        }


        WxPayService wxPayService = new WxPayService(configStorage);
        wxPayService.setRequestTemplateConfigStorage(httpConfigStorage);
        return wxPayService;
    }

    @Override
    public TransactionType getTransactionType(String name) {
        return WxTransactionType.valueOf(name);
    }


}

