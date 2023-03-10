package com.cn.xiguaapp.pay.alipay.core;

import com.cn.xiguapp.common.core.utils.StringUtils;
import com.cn.xiguaapp.xiguaapp.java.common.core.base.Order;
import com.cn.xiguaapp.xiguaapp.java.common.utils.Util;
import lombok.Data;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author xiguaapp
 * @desc 交易结算信息
 * @since 1.0 19:49
 */
@Data
public class OrderSettle implements Order {
    /**
     * 结算请求流水号 开发者自行生成并保证唯一性
     */
    private String outRequestNo;
    /**
     * 支付宝订单号
     */
    private String tradeNo;
    /**
     * 分账支出方账户，类型为userId，本参数为要分账的支付宝账号对应的支付宝唯一用户号。以2088开头的纯16位数字。
     */
    private String transOut;

    /**
     * 分账收入方账户，类型为userId，本参数为要分账的支付宝账号对应的支付宝唯一用户号。以2088开头的纯16位数字。
     */
    private String transIn;

    /**
     * 分账的金额，单位为元
     */
    private BigDecimal amount;
    /**
     * 分账信息中分账百分比。取值范围为大于0，少于或等于100的整数。
     */
    private Integer amountPercentage;

    /**
     * 分账描述
     */
    private String desc;

    /**
     * 操作员id
     */
    private String operatorId;

    /**
     * 订单附加信息，可用于预设未提供的参数，这里会覆盖以上所有的订单信息，
     */
    private Map<String, Object> attr;

    /**
     * 获取分账明细信息
     * @return 分账明细信息
     */
    public Map<String, Object> toRoyaltyParameters(){
        Map<String, Object> royalty = new TreeMap<String, Object>();

        if (StringUtils.isNotEmpty(transOut)){
            royalty.put("trans_out",  transOut);
        }

        if (StringUtils.isNotEmpty( transIn)){
            royalty.put("trans_in",  transIn);
        }
        if (null !=  amount){
            royalty.put("amount", Util.conversionAmount(amount));
        }
        if (null !=  amountPercentage){
            royalty.put("amount_percentage", amountPercentage);
        }

        if (StringUtils.isNotEmpty(  desc)){
            royalty.put(" desc",   desc);
        }
        return royalty;
    }

    /**
     * 请求参数的集合，最大长度不限，除公共参数外所有请求参数都必须放在这个参数中传递，具体参照各产品快速接入文档
     * @return 请求参数的集合
     */
    public Map<String, Object> toBizContent(){
        Map<String, Object> bizContent = new TreeMap<String, Object>();
        bizContent.put("out_request_no", outRequestNo);
        bizContent.put("trade_no", tradeNo);
        bizContent.put("royalty_parameters", toRoyaltyParameters());
        if (StringUtils.isNotEmpty(operatorId)){
            bizContent.put("operator_id", operatorId);
        }
        return bizContent;
    }

    @Override
    public Map<String, Object> getAttrs() {
        if (null == attr){
            attr = new HashMap<>();
        }
        return attr;
    }

    @Override
    public Object getAttr(String key) {
        return getAttrs().get(key);
    }


    /**
     * 添加订单信息
     * @param key key
     * @param value 值
     */
    @Override
    public void addAttr(String key, Object value) {
        getAttrs().put(key, value);
    }
}
