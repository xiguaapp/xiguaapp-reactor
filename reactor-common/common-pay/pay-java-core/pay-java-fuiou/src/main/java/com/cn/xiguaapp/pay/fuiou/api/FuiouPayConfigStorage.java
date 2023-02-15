package com.cn.xiguaapp.pay.fuiou.api;

import com.cn.xiguaapp.xiguaapp.java.common.api.impl.BasePayConfigStorage;

/**
 * @author xiguaapp
 * @desc 富友支付配置
 * @since 1.0 21:04
 */
public class FuiouPayConfigStorage extends BasePayConfigStorage {
    /**
     * 商户代码
     */
    private String mchntCd;

    /**
     * 应用id
     *
     * @return 空
     */
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
        return mchntCd;
    }

    public String getMchntCd() {
        return mchntCd;
    }

    public void setMchntCd(String mchntCd) {
        this.mchntCd = mchntCd;
    }

    @Override
    public String getSeller() {
        return null;
    }

}
