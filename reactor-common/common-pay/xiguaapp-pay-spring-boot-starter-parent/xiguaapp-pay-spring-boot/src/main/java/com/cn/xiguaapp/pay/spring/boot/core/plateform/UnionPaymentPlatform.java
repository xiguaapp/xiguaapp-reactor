package com.cn.xiguaapp.pay.spring.boot.core.plateform;

import com.cn.xiguaapp.xiguaapp.java.common.api.PayConfigStorage;
import com.cn.xiguaapp.xiguaapp.java.common.api.PayService;
import com.cn.xiguaapp.xiguaapp.java.common.core.base.CertStoreType;
import com.cn.xiguaapp.xiguaapp.java.common.core.base.TransactionType;
import com.cn.xiguaapp.xiguaapp.java.common.http.HttpConfigStorage;
import com.cn.xiguaapp.pay.spring.boot.core.merchant.PaymentPlatform;
import com.cn.xiguaapp.pay.spring.boot.core.merchant.info.CommonPaymentPlatformMerchantDetails;
import com.cn.xiguaapp.pay.unino.api.UnionPayConfigStorage;
import com.cn.xiguaapp.pay.unino.api.UnionPayService;
import com.cn.xiguaapp.pay.unino.constants.UnionTransactionType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

/**
 * @author xiguaapp
 * @desc 银联支付平台
 * @since 1.0 23:43
 */
@Configuration(UnionPaymentPlatform.platformName)
@ConditionalOnMissingBean(UnionPaymentPlatform.class)
//@ConditionalOnClass(name = {"com.cn.xiguaapp.pay.union.api.UnionPayConfigStorage"})
@ConditionalOnClass(value = UnionPayConfigStorage.class)
public class UnionPaymentPlatform implements PaymentPlatform {
    protected final Log LOG = LogFactory.getLog(UnionPaymentPlatform.class);
    public static final String platformName = "unionPay";



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
        if (payConfigStorage instanceof UnionPayConfigStorage) {
            return new UnionPayService((UnionPayConfigStorage) payConfigStorage);
        }
        UnionPayConfigStorage configStorage = new UnionPayConfigStorage();
        configStorage.setMerId(payConfigStorage.getPid());
        //是否为证书签名
        configStorage.setCertSign(true);
        configStorage.setPid(payConfigStorage.getPid());
        configStorage.setNotifyUrl(payConfigStorage.getNotifyUrl());
        configStorage.setReturnUrl(payConfigStorage.getReturnUrl());
        configStorage.setSignType(payConfigStorage.getSignType());
        configStorage.setPayType(payConfigStorage.getPayType());
//        configStorage.setMsgType(payConfigStorage.getMsgType());
        configStorage.setInputCharset(payConfigStorage.getInputCharset());
        configStorage.setTest(payConfigStorage.isTest());
        if (payConfigStorage instanceof CommonPaymentPlatformMerchantDetails) {
            CommonPaymentPlatformMerchantDetails merchantDetails = (CommonPaymentPlatformMerchantDetails) payConfigStorage;
            //设置证书对应的存储方式
            configStorage.setCertStoreType(merchantDetails.getCertStoreType());
            try {
                //中级证书路径
                configStorage.setAcpMiddleCert(merchantDetails.getKeyPublicCertInputStream());
                //根证书路径
                configStorage.setAcpRootCert(merchantDetails.getKeyCertInputStream());
                // 私钥证书路径
                configStorage.setKeyPrivateCert(merchantDetails.getKeystoreInputStream());
                //这里转变为流的方式
                configStorage.setCertStoreType(CertStoreType.INPUT_STREAM);
            } catch (IOException e) {
                LOG.error(e);
            }
            //私钥证书对应的密码
            configStorage.setKeyPrivateCertPwd(merchantDetails.getKeystorePwd());

        }else {


        }

        return new UnionPayService(configStorage);
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
        return UnionTransactionType.valueOf(name);
    }


}
