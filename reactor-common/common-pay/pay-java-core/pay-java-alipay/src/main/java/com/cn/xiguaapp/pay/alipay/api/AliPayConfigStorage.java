package com.cn.xiguaapp.pay.alipay.api;

import com.cn.xiguaapp.pay.alipay.core.CertEnvironment;
import com.cn.xiguaapp.xiguaapp.java.common.api.CertStore;
import com.cn.xiguaapp.xiguaapp.java.common.api.impl.BasePayConfigStorage;
import com.cn.xiguaapp.xiguaapp.java.common.exception.PayErrorException;
import com.cn.xiguaapp.xiguaapp.java.common.result.PayException;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author xiguaapp
 * @desc 支付配置存储
 *  以下证书签名相关触发前提是 {@link BasePayConfigStorage#isCertSign}等于true的情况。不然走的就是普通的方式
 * @since 1.0 20:09
 */
public class AliPayConfigStorage extends BasePayConfigStorage {

    /**
     * ISV代商户代用，指定appAuthToken
     */
    private String appAuthToken;
    /**
     * 商户应用id
     */
    private String appId;
    /**
     * 商户签约拿到的pid,partner_id的简称，合作伙伴身份等同于 partner
     */
    private String pid;

    /**
     * 商户收款账号
     */
    private String seller;


    /**
     * 应用公钥证书
     */
    private Object merchantCert;

    /**
     * 支付宝公钥证书
     */
    private Object aliPayCert;
    /**
     * 支付宝CA证书，根证书
     */
    private Object aliPayRootCert;

    /**
     * 证书存储类型
     */
    private CertStore certStoreType;

    /**
     * 证书信息
     */
    private CertEnvironment certEnvironment;


    public String getAppAuthToken() {
        return appAuthToken;
    }

    public void setAppAuthToken(String appAuthToken) {
        this.appAuthToken = appAuthToken;
    }

    public void setAppid(String appId) {
        this.appId = appId;
    }

    @Override
    @Deprecated
    public String getAppid() {
        return appId;
    }

    /**
     * 应用id
     * 纠正名称
     *
     * @return 应用id
     */
    @Override
    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    @Override
    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    @Override
    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public Object getMerchantCert() {
        return merchantCert;
    }

    public void setMerchantCert(Object merchantCert) {
        this.merchantCert = merchantCert;
    }

    public Object getAliPayCert() {
        return aliPayCert;
    }

    public void setAliPayCert(Object aliPayCert) {
        this.aliPayCert = aliPayCert;
    }

    public Object getAliPayRootCert() {
        return aliPayRootCert;
    }

    public void setAliPayRootCert(Object aliPayRootCert) {
        this.aliPayRootCert = aliPayRootCert;
    }

    public CertStore getCertStoreType() {
        return certStoreType;
    }

    public void setCertStoreType(CertStore certStoreType) {
        this.certStoreType = certStoreType;
    }

    public CertEnvironment getCertEnvironment() {
        return certEnvironment;
    }

    public void setCertEnvironment(CertEnvironment certEnvironment) {
        this.certEnvironment = certEnvironment;
    }

    /**
     * 初始化证书信息
     */
    public void loadCertEnvironment() {
        if (!isCertSign() || null != this.certEnvironment) {
            return;
        }
        try (InputStream merchantCertStream = certStoreType.getInputStream(merchantCert);
             InputStream aliPayCertStream = certStoreType.getInputStream(aliPayCert);
             InputStream aliPayRootCertStream = certStoreType.getInputStream(aliPayRootCert)) {
            this.certEnvironment = new CertEnvironment(merchantCertStream, aliPayCertStream, aliPayRootCertStream);
        }
        catch (IOException e) {
            throw new PayErrorException(new PayException("读取证书异常", e.getMessage()));
        }
    }
}
