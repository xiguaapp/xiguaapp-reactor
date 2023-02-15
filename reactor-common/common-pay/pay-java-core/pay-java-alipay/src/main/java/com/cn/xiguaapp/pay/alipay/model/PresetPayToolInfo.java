package com.cn.xiguaapp.pay.alipay.model;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * @author xiguaapp
 * @desc 退回的前置资产列表
 * @since 1.0 19:48
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class PresetPayToolInfo {
    /**
     * 必填 	32前置资产金额 	12.21
     */
    private BigDecimal[] amount;

    /**
     * 前置资产类型编码，和收单支付传入的preset_pay_tool里面的类型编码保持一致。盒马礼品卡:HEMA；抓猫猫红包:T_CAT_COUPON
     */
    @JSONField(name = "assert_type_code")
    private String assertTypeCode;
}
