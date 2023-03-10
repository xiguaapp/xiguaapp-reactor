package com.cn.xiguaapp.pay.wx.youdian.test;

import com.cn.xiguaapp.xiguaapp.java.common.api.PayService;
import com.cn.xiguaapp.xiguaapp.java.common.core.base.PayOrder;
import com.cn.xiguaapp.pay.wx.youdian.api.WxYouDianPayConfigStorage;
import com.cn.xiguaapp.pay.wx.youdian.api.WxYouDianPayService;
import com.cn.xiguaapp.pay.wx.youdian.core.YoudianTransactionType;

import java.awt.image.BufferedImage;
import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

/**
 * @author xiguaapp
 * @desc 友店微信支付测试
 * @since 1.0 22:21
 */
public class YoudianTest {
    public static void main(String[] args) {


        WxYouDianPayConfigStorage wxPayConfigStorage = new WxYouDianPayConfigStorage();
        wxPayConfigStorage.setKeyPrivate("友店所提供的加密串");
        wxPayConfigStorage.setKeyPublic("线下支付异步通知加签密钥");
//            wxPayConfigStorage.setNotifyUrl(account.getNotifyUrl());
//            wxPayConfigStorage.setReturnUrl(account.getReturnUrl());
        wxPayConfigStorage.setSeller("友店账号");
        wxPayConfigStorage.setSignType("签名方式");
        wxPayConfigStorage.setInputCharset("utf-8");
        //是否为测试账号，沙箱环境 此处暂未实现
        wxPayConfigStorage.setTest(true);
        //支付服务
        PayService service =   new WxYouDianPayService(wxPayConfigStorage);
        //支付订单基础信息
        PayOrder payOrder = new PayOrder("订单title", "摘要",  BigDecimal.valueOf(0.01) , UUID.randomUUID().toString().replace("-", ""));
        /*-----------扫码付-------------------*/
        payOrder.setTransactionType(YoudianTransactionType.NATIVE);
        //获取扫码付的二维码
        BufferedImage image = service.genQrPay(payOrder);
        /*-----------/扫码付-------------------*/


        /*-----------回调处理-------------------*/
        Map<String, Object> params = null;
//        HttpServletRequest request
//        params = service.getParameter2Map(request.getParameterMap(), request.getInputStream());
        if (service.verify(params)){
            System.out.println("支付成功");
            return;
        }
        System.out.println("支付失败");


        /*-----------回调处理-------------------*/
    }
}
