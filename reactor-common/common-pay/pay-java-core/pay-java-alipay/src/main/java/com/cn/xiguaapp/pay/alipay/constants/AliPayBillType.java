package com.cn.xiguaapp.pay.alipay.constants;

import com.cn.xiguapp.common.core.time.DateTimeUtils;
import com.cn.xiguaapp.xiguaapp.java.common.core.base.BillType;

/**
 * @author xiguaapp
 * @desc 支付宝账单类型
 * @since 1.0 20:07
 */
public enum AliPayBillType implements BillType {
    /**
     * 商户基于支付宝交易收单的业务账单；每日账单
     */
    TRADE_DAY("trade", DateTimeUtils.YEAR_MONTH_DAY),
    /**
     * 商户基于支付宝交易收单的业务账单；每月账单
     */
    TRADE_MONTH("trade", DateTimeUtils.YEAR_MONTH),
    /**
     * 基于商户支付宝余额收入及支出等资金变动的帐务账单；每日账单
     */
    SIGNCUSTOMER_DAY("signcustomer", DateTimeUtils.YEAR_MONTH_DAY),
    /**
     * 基于商户支付宝余额收入及支出等资金变动的帐务账单；每月账单
     */
    SIGNCUSTOMER_MONTH("signcustomer", DateTimeUtils.YEAR_MONTH),

    ;

    /**
     * 账单类型
     */
    private String type;
    /**
     * 日期格式化表达式
     */
    private String datePattern;

    AliPayBillType(String type, String datePattern) {
        this.type = type;
        this.datePattern = datePattern;
    }

    /**
     * 获取类型名称
     *
     * @return 类型
     */
    @Override
    public String getType() {
        return type;
    }

    /**
     * 获取类型对应的日期格式化表达式
     *
     * @return 日期格式化表达式
     */
    @Override
    public String getDatePattern() {
        return datePattern;
    }

    /**
     * 获取文件类型
     *
     * @return 文件类型
     */
    @Override
    public String getFileType() {
        return null;
    }


    /**
     * 自定义属性
     *
     * @return 自定义属性
     */
    @Override
    public String getCustom() {
        return null;
    }

}
