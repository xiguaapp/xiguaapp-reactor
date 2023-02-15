package com.cn.xiguaapp.pay.test.controller;

import com.cn.xiguaapp.xiguaapp.java.common.api.PayMessageHandler;
import com.cn.xiguaapp.xiguaapp.java.common.api.PayService;
import com.cn.xiguaapp.xiguaapp.java.common.api.impl.DefaultPayMessageHandler;
import com.cn.xiguaapp.pay.spring.boot.core.MerchantPayServiceManager;
import com.cn.xiguaapp.pay.spring.boot.core.bean.MerchantPayOrder;
import com.cn.xiguaapp.pay.spring.boot.core.merchant.PaymentPlatformMerchantDetails;
import com.cn.xiguaapp.pay.spring.boot.core.provider.MerchantDetailsManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.UUID;

/**
 * @author xiguaapp
 * @desc
 * @since 1.0 00:30
 */
@RequestMapping("pay")
@Controller
public class PayMerchantController {
    @Autowired
    private MerchantPayServiceManager manager;


    @Autowired
    private MerchantDetailsManager<PaymentPlatformMerchantDetails> detailsService;

    /**
     * 网页支付
     * @return 网页
     */
    @ResponseBody
    @RequestMapping(value = "delete.json")
    public String delete(String detailsId) {
        detailsService.deleteMerchant(detailsId);
        return "ok";
    }

    /**
     * 网页支付
     * @param detailsId 列表id
     * @param wayTrade 交易方式
     * @return 网页
     */
    @ResponseBody
    @RequestMapping(value = "toPay.html", produces = "text/html;charset=UTF-8")
    public String toPay(String detailsId, String wayTrade, BigDecimal price) {
        MerchantPayOrder payOrder = new MerchantPayOrder(detailsId, wayTrade, "订单title", "摘要", null == price ? new BigDecimal(0.01) : price, UUID.randomUUID().toString().replace("-", ""));
        return manager.toPay(payOrder);
    }
    /**
     * 二维码
     * @param detailsId 列表id
     * @param wayTrade 交易方式
     * @return 二维码
     */
    @ResponseBody
    @RequestMapping(value = "toQrPay.jpg", produces = "image/jpeg;charset=UTF-8")
    public byte[] toQrPay(String detailsId, String wayTrade, BigDecimal price) throws IOException {
        MerchantPayOrder payOrder = new MerchantPayOrder(detailsId, wayTrade, "订单title", "摘要", null == price ? new BigDecimal(0.01) : price, UUID.randomUUID().toString().replace("-", ""));
        return manager.toQrPay(payOrder);
    }
    /**
     * 二维码信息
     * @param detailsId 列表id
     * @param wayTrade 交易方式
     * @return 二维码信息
     */
    @ResponseBody
    @RequestMapping(value = "getQrPay.json")
    public String getQrPay(String detailsId, String wayTrade, BigDecimal price) throws IOException {
        MerchantPayOrder payOrder = new MerchantPayOrder(detailsId, wayTrade, "订单title", "摘要", null == price ? new BigDecimal(0.01) : price, UUID.randomUUID().toString().replace("-", ""));
        return manager.getQrPay(payOrder);
    }


    /**
     * 支付回调地址
     * @param request 请求
     * @param detailsId 列表id
     * @return 支付是否成功
     * @throws IOException IOException
     * 拦截器相关增加， 详情查看{@link 、PayService#addPayMessageInterceptor(PayMessageInterceptor)}
     * <p>
     * 业务处理在对应的PayMessageHandler里面处理，在哪里设置PayMessageHandler，详情查看{@link PayService#setPayMessageHandler(PayMessageHandler)}
     * </p>
     * 如果未设置 {@link PayMessageHandler} 那么会使用默认的 {@link DefaultPayMessageHandler}
     */
    @RequestMapping(value = "payBack{detailsId}.json")
    public String payBack(HttpServletRequest request, @PathVariable String detailsId) throws IOException {
        //业务处理在对应的PayMessageHandler里面处理，在哪里设置PayMessageHandler，详情查看PayService.setPayMessageHandler()
        return manager.payBack(detailsId, request.getParameterMap(), request.getInputStream());
    }
}
