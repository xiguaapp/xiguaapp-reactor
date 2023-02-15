package com.cn.xiguaapp.pay.yiji.api;

import com.cn.xiguaapp.xiguaapp.java.common.api.impl.BasePayConfigStorage;

/**
 * @author xiguaapp
 * @desc 易极付配置存储
 * @since 1.0 22:26
 */
public class YiJiPayConfigStorage extends BasePayConfigStorage {


    /**
     * 易极付分配的商户号 合作者id
     */
    private String partnerId;

    /**
     * 卖家id
     */
    private String sellerUserId;

    public String getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(String partnerId) {
        this.partnerId = partnerId;
    }

    @Override
    public String getAppid() {
        return null;
    }

    /**
     * 应用id
     * 纠正名称
     *
     * @return 应用id
     */
    @Override
    public String getAppId() {
        return null;
    }


    /**
     * 合作商唯一标识
     */
    @Override
    public String getPid() {
        return partnerId;
    }


    @Override
    public String getSeller() {
        return sellerUserId;
    }

    public String getSellerUserId() {
        return sellerUserId;
    }

    public void setSellerUserId(String sellerUserId) {
        this.sellerUserId = sellerUserId;
    }

    /**
     * 为商户平台设置的密钥key
     *
     * @return 密钥
     */
    public String getSecretKey() {
        return getKeyPrivate();
    }

    public void setSecretKey(String secretKey) {
        setKeyPrivate(secretKey);
    }


}

