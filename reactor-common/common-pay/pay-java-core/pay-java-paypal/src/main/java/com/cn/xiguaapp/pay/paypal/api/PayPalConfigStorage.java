package com.cn.xiguaapp.pay.paypal.api;

import com.cn.xiguaapp.xiguaapp.java.common.api.impl.BasePayConfigStorage;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @author xiguaapp
 * @desc 贝宝支付配置存储
 * @since 1.0 21:33
 */
public class PayPalConfigStorage extends BasePayConfigStorage {

    private String clientId;

    @Override
    @Deprecated
    public String getAppid() {
        return clientId;
    }

    /**
     * 应用id
     * 纠正名称
     *
     * @return 应用id
     */
    @Override
    public String getAppId() {
        return clientId;
    }

    @Override
    public String getPid() {
        return clientId;
    }

    @Override
    public String getSeller() {
        return clientId;
    }

    public String getClientID() {
        return clientId;
    }

    public void setClientID(String clientId) {
        this.clientId = clientId;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }


    public String getClientSecret() {
        return getKeyPrivate();
    }

    public void setClientSecret(String clientSecret) {
        setKeyPrivate(clientSecret);
    }


    /**
     * 设置取消页面的url
     * <pre>
     * 注意：这里不是异步回调的通知
     * IPN 地址设置的路径：https://developer.paypal.com/developer/ipnSimulator/
     * </pre>
     *
     * @param cancelUrl 取消页面的url
     */
    public void setCancelUrl(String cancelUrl) {
        setNotifyUrl(cancelUrl);
    }

    /**
     * 获取取消页面的url
     * <pre>
     * 注意：这里不是异步回调的通知
     * IPN 地址设置的路径：https://developer.paypal.com/developer/ipnSimulator/
     * </pre>
     *
     * @return 取消页面的url
     */
    public String getCancelUrl() {
        return getNotifyUrl();
    }

    public PayPalConfigStorage() {
        setAccessTokenLock(new ReentrantLock());
    }
}

