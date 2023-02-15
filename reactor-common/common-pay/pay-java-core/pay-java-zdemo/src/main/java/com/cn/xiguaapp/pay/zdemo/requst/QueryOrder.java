package com.cn.xiguaapp.pay.zdemo.requst;

import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author xiguaapp
 * @desc 订单辅助接口
 * @since 1.0 22:38
 */
@Data
@ToString
public class QueryOrder {
    private Integer payId;
    //    支付平台订单号
    private String tradeNo;

    //    商户单号
    private String outTradeNo;
    //    退款金额
    private BigDecimal refundAmount;
    //    总金额
    private BigDecimal totalAmount;
    //    账单时间：具体请查看对应支付平台
    private Date billDate;
    //    账单时间：具体请查看对应支付平台
    private String billType;
    //    支付平台订单号或者账单日期
    private Object tradeNoOrBillDate;
    //    商户单号或者 账单类型
    private String outTradeNoBillType;
    //    交易类型
    private String transactionType;
}
