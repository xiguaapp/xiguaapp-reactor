package com.cn.xiguaapp.pay.wx.constants;

import com.cn.xiguaapp.xiguaapp.java.common.core.base.TransferOrder;
import com.cn.xiguaapp.xiguaapp.java.common.core.base.TransferType;

import java.util.Map;

/**
 * @author xiguaapp
 * @desc 微信转账类型
 * @since 1.0 22:02
 */
public enum WxTransferType implements TransferType {
    /**
     * 转账到零钱
     */
    TRANSFERS("mmpaymkttransfers/promotion/transfers"),
    /**
     * 查询转账到零钱的记录
     */
    GETTRANSFERINFO("mmpaymkttransfers/gettransferinfo"),
    /**
     * 转账到银行卡
     */
    PAY_BANK("mmpaysptrans/pay_bank"),
    /**
     * 查询转账到银行卡的记录
     */
    QUERY_BANK("mmpaysptrans/query_bank"),

    ;

    WxTransferType(String method) {
        this.method = method;
    }

    private String method;
    @Override
    public String getType() {
        return this.name();
    }
    @Override
    public String getMethod() {
        return this.method;
    }


    @Override
    public Map<String, Object> setAttr(Map<String, Object> attr, TransferOrder order) {
        return attr;
    }
}
