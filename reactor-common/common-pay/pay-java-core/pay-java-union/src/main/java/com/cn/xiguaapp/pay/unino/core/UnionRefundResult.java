package com.cn.xiguaapp.pay.unino.core;

import com.alibaba.fastjson.JSONObject;
import com.cn.xiguaapp.xiguaapp.java.common.core.base.BaseRefundResult;
import com.cn.xiguaapp.xiguaapp.java.common.core.base.CurType;
import lombok.*;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Map;

/**
 * @author xiguaapp
 * @desc 银联退款结果
 * @since 1.0 21:44
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UnionRefundResult extends BaseRefundResult {

    /**
     * 二维码数据
     */
    private String qrCode;
    /**
     * 签名
     */
    private String signature;
    /**
     * 签名方法
     */
    private String signMethod;
    /**
     * 应答码
     */
    private String respCode;
    /**
     * 应答信息
     */
    private String respMsg;
    /**
     * 签名公钥证书
     */
    private String signPubKeyCert;
    /**
     * 版本号
     */
    private String version;
    /**
     * 编码方式
     */
    private String encoding;
    /**
     * 产品类型
     */
    private String bizType;
    /**
     * 订单发送时间
     */
    private String txnTime;

    /**
     * 交易类型
     */
    private String txnType;
    /**
     * 交易子类
     */
    private String txnSubType;
    /**
     * 接入类型
     * 0：商户直连接入
     * 1：收单机构接入
     * 2：平台商户接入
     */
    private String accessType;
    /**
     * 请求方保留域
     */
    private String reqReserved;
    /**
     * 商户代码
     */
    private String merId;
    /**
     * 商户订单号
     */
    private String orderId;
    /**
     * 保留域
     */
    private String reserved;


    /**
     * 获取退款请求结果状态码
     *
     * @return 状态码
     */
    @Override
    public String getCode() {
        return respCode;
    }

    /**
     * 获取退款请求结果状态提示信息
     *
     * @return 提示信息
     */
    @Override
    public String getMsg() {
        return respMsg;
    }

    /**
     * 返回业务结果状态码
     *
     * @return 业务结果状态码
     */
    @Override
    public String getResultCode() {
        return null;
    }

    /**
     * 返回业务结果状态提示信息
     *
     * @return 业务结果状态提示信息
     */
    @Override
    public String getResultMsg() {
        return null;
    }

    /**
     * 退款金额
     *
     * @return 退款金额
     */
    @Override
    public BigDecimal getRefundFee() {
        return null;
    }

    /**
     * 退款币种信息
     *
     * @return 币种信息
     */
    @Override
    public CurType getRefundCurrency() {
        return null;
    }

    /**
     * 支付平台交易号
     * 发起支付时 支付平台(如支付宝)返回的交易订单号
     *
     * @return 支付平台交易号
     */
    @Override
    public String getTradeNo() {
        return null;
    }

    /**
     * 支付订单号
     * 发起支付时，用户系统的订单号
     *
     * @return 支付订单号
     */
    @Override
    public String getOutTradeNo() {
        return orderId;
    }

    /**
     * 商户退款单号
     *
     * @return 商户退款单号
     */
    @Override
    public String getRefundNo() {
        return null;
    }

    public static final UnionRefundResult create(Map<String, Object> result){
        UnionRefundResult refundResult = new JSONObject(result).toJavaObject(UnionRefundResult.class);
        refundResult.setAttrs(result);
        return refundResult;
    }
}

