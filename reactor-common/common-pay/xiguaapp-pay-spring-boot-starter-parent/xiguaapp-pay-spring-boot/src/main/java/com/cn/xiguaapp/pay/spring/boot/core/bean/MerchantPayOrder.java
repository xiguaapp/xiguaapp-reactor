package com.cn.xiguaapp.pay.spring.boot.core.bean;

import com.cn.xiguaapp.xiguaapp.java.common.core.base.PayOrder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author xiguaapp
 * @desc 商户支付订单
 * @since 1.0 23:25
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class MerchantPayOrder extends PayOrder {

    /**
     * 列表id
     */
    private String  detailsId;
    /**
     * 交易类型，交易方式，
     * 本字段与{@link PayOrder#getTransactionType()}相同。
     *
     *  例如，网页支付，扫码付等等
     */
    private String wayTrade;

    public MerchantPayOrder(String detailsId, String wayTrade) {
        this.detailsId = detailsId;
        this.wayTrade = wayTrade;
    }



    public MerchantPayOrder(String detailsId, String wayTrade, String subject, String body, BigDecimal price, String outTradeNo) {
        super(subject, body, price, outTradeNo);
        this.detailsId = detailsId;
        this.wayTrade = wayTrade;
    }

}
