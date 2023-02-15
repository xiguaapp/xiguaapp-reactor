package com.cn.xiguaapp.pay.baidu.api;

import com.cn.xiguaapp.xiguaapp.java.common.api.impl.BasePayConfigStorage;

/**
 * @author xiguaapp
 * @desc 百度支付配置
 * @since 1.0 20:49
 */
public class BaiduPayConfigStorage extends BasePayConfigStorage {
    private String appId;
    private String dealId;

    @Override
    @Deprecated
    public String getAppid() {
        return this.appId;
    }

    @Override
    public String getPid() {
        return getDealId();
    }

    @Override
    public String getSeller() {
        throw new UnsupportedOperationException("不支持");
    }

    public String getDealId() {
        return dealId;
    }

    public void setDealId(String dealId) {
        this.dealId = dealId;
    }

    public String getAppKey() {
        return this.getKeyPrivate();
    }

    public void setAppKey(String appKey) {
        setKeyPrivate(appKey);
    }

    @Override
    public String getKeyPublic() {
        return super.getKeyPrivate();
    }

    @Override
    public void setKeyPublic(String keyPublic) {
        super.setKeyPublic(keyPublic);
    }

    public void setAppid(String appId) {
        this.appId = appId;
    }

    @Override
    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }
}
