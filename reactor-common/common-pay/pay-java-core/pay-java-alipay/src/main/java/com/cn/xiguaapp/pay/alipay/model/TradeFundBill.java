package com.cn.xiguaapp.pay.alipay.model;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * @author xiguaapp
 * @desc 退款使用的资金渠道。
 *  只有在签约中指定需要返回资金明细，或者入参的query_options中指定时才返回该字段信息。
 * @since 1.0 19:47
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class TradeFundBill {
    /**
     * 交易使用的资金渠道，详见 支付渠道列表 	ALIPAYACCOUNT
     */
    @JSONField(name = "fund_channel")
    private String fundChannel;
    /**
     * 银行卡支付时的银行代码 	CEB
     */
    @JSONField(name = "bank_code")
    private String bankCode;
    /**
     * 该支付工具类型所使用的金额 	10
     */
    private BigDecimal amount;

    /**
     * 渠道实际付款金额 	11.21
     */
    @JSONField(name = "real_amount")
    private BigDecimal realAmount;
    /**
     * 渠道所使用的资金类型,目前只在资金渠道(fund_channel)是银行卡渠道(BANKCARD)的情况下才返回该信息(DEBIT_CARD:借记卡,CREDIT_CARD:信用卡,MIXED_CARD:借贷合一卡) 	DEBIT_CARD
     */
    @JSONField(name = "fund_type")
    private String fundType;
}
