package com.cn.xiguaapp.pay.wx.api;

import com.cn.xiguaapp.pay.wx.core.RedpackOrder;

import java.util.Map;

/**
 * @author xiguaapp
 * @desc 微信红包服务
 * @since 1.0 22:04
 */
public interface WxRedPackService {
    /**
     * 微信发红包
     *
     * @param redpackOrder 红包实体
     * @return 返回发红包实体后的结果
     */
    Map<String, Object> sendredpack(RedpackOrder redpackOrder);

    /**
     * 查询红包记录
     * 用于商户对已发放的红包进行查询红包的具体信息，可支持普通红包和裂变包
     * 查询红包记录API只支持查询30天内的红包订单，30天之前的红包订单请登录商户平台查询。
     *
     * @param mchBillno 商户发放红包的商户订单号
     * @return 返回查询结果
     */
    Map<String, Object> gethbinfo(String mchBillno);
}
