package com.cn.xiguaapp.pay.baidu.constant;

import com.cn.xiguaapp.xiguaapp.java.common.core.base.TransactionType;

/**
 * @author xiguaapp
 * @desc 百度交易类型
 *  说明交易类型主要用于支付接口调用参数所需
 * @since 1.0 20:47
 */
public enum BaiduTransactionType implements TransactionType {
    /**
     * 查询支付状态
     */
    PAY_QUERY("https://dianshang.baidu.com/platform/entity/openapi/queryorderdetail", null),
    /**
     * 取消核销
     */
    REFUND_QUERY("https://nop.nuomi.com/nop/server/rest", "nuomi.cashier.syncorderstatus"),
    /**
     * 下载资金账单
     */
    DOWNLOAD_BILL("https://openapi.baidu.com/rest/2.0/smartapp/pay/paymentservice/capitaBill", null),
    /**
     * 下载订单对账单
     */
    DOWNLOAD_ORDER_BILL("https://openapi.baidu.com/rest/2.0/smartapp/pay/paymentservice/orderBill", null),
    /**
     * 申请退款
     */
    APPLY_REFUND("https://nop.nuomi.com/nop/server/rest", "nuomi.cashier.applyorderrefund");
    private final String method;
    private final String url;

    BaiduTransactionType( String url, String method) {
        this.url = url;
        this.method = method;
    }

    @Override
    public String getType() {
        return this.name();
    }

    @Override
    public String getMethod() {
        return this.method;
    }

    public String getUrl() {
        return url;
    }
}
