package com.cn.xiguaapp.pay.spring.boot.core;

import com.cn.xiguaapp.xiguaapp.java.common.api.PayMessageHandler;
import com.cn.xiguaapp.xiguaapp.java.common.api.PayMessageInterceptor;
import com.cn.xiguaapp.xiguaapp.java.common.api.PayService;
import com.cn.xiguaapp.xiguaapp.java.common.api.impl.DefaultPayMessageHandler;
import com.cn.xiguaapp.pay.java.common.core.base.*;
import com.cn.xiguaapp.pay.spring.boot.core.bean.MerchantPayOrder;
import com.cn.xiguaapp.pay.spring.boot.core.bean.MerchantQueryOrder;
import com.cn.xiguaapp.pay.spring.boot.core.merchant.MerchantDetailsService;
import com.cn.xiguaapp.pay.spring.boot.core.merchant.PaymentPlatformMerchantDetails;
import org.springframework.beans.factory.annotation.Autowired;

import javax.imageio.ImageIO;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * @author xiguaapp
 * @desc 商户支付服务
 * @since 1.0 23:32
 */
public class MerchantPayServiceManager implements PayServiceManager {

    @Autowired
    private MerchantDetailsService<PaymentPlatformMerchantDetails> detailsService;


    /**
     * 跳到支付页面
     * 针对实时支付,即时付款
     *
     * @param payOrder 商户支付订单信息
     * @return 跳到支付页面
     */
    @Override
    public String toPay(MerchantPayOrder payOrder) {
        PaymentPlatformMerchantDetails details = detailsService.loadMerchantByMerchantId(payOrder.getDetailsId());
        payOrder.setTransactionType(details.getPaymentPlatform().getTransactionType(payOrder.getWayTrade()));
        PayService payService = details.getPayService();
        Map<String, Object> orderInfo = payService.orderInfo(payOrder);
        return payService.buildRequest(orderInfo, MethodType.POST);
    }

    /**
     * 获取支付预订单信息
     *
     * @param payOrder 商户支付订单信息
     * @return 支付预订单信息
     */
    @Override
    public Map<String, Object> app(MerchantPayOrder payOrder) {
        PaymentPlatformMerchantDetails details = detailsService.loadMerchantByMerchantId(payOrder.getDetailsId());
        payOrder.setTransactionType(details.getPaymentPlatform().getTransactionType(payOrder.getWayTrade()));
        PayService payService = details.getPayService();
        return payService.app(payOrder);
    }

    /**
     * 获取支付预订单信息
     *
     * @param payOrder 商户支付订单信息
     * @return 支付预订单信息
     */
    @Override
    public Map<String, Object> getOrderInfo(MerchantPayOrder payOrder) {
        PaymentPlatformMerchantDetails details = detailsService.loadMerchantByMerchantId(payOrder.getDetailsId());
        payOrder.setTransactionType(details.getPaymentPlatform().getTransactionType(payOrder.getWayTrade()));
        PayService payService = details.getPayService();
        Map<String, Object> orderInfo = payService.orderInfo(payOrder);
        return orderInfo;
    }

    /**
     * 刷卡付,pos主动扫码付款(条码付)
     * 刷脸付
     * @param payOrder 商户支付订单信息
     * @return 支付结果
     */
    @Override
    public Map<String, Object> microPay(MerchantPayOrder payOrder) {
        PaymentPlatformMerchantDetails details = detailsService.loadMerchantByMerchantId(payOrder.getDetailsId());
        payOrder.setTransactionType(details.getPaymentPlatform().getTransactionType(payOrder.getWayTrade()));
        //支付结果
        Map<String, Object> params = details.getPayService().microPay(payOrder);
        return params;
    }

    /**
     * 获取二维码图像
     * 二维码支付
     *
     * @param payOrder 商户支付订单信息
     * @return 二维码图像
     * @throws IOException IOException
     */
    @Override
    public byte[] toQrPay(MerchantPayOrder payOrder) throws IOException {
        //获取对应的支付账户操作工具（可根据账户id）
        PaymentPlatformMerchantDetails details = detailsService.loadMerchantByMerchantId(payOrder.getDetailsId());
        payOrder.setTransactionType(details.getPaymentPlatform().getTransactionType(payOrder.getWayTrade()));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        ImageIO.write(details.getPayService().genQrPay(payOrder), "JPEG", baos);
        return baos.toByteArray();
    }
    /**
     * 获取二维码信息
     * 二维码支付
     *
     * @param payOrder 商户支付订单信息
     * @return 二维码信息
     */
    @Override
    public String getQrPay(MerchantPayOrder payOrder) {
        PaymentPlatformMerchantDetails details = detailsService.loadMerchantByMerchantId(payOrder.getDetailsId());
        payOrder.setTransactionType(details.getPaymentPlatform().getTransactionType(payOrder.getWayTrade()));
        return  details.getPayService().getQrPay(payOrder);
    }

    /**
     * 支付回调地址
     * 方式二
     *
     * @param detailsId    商户列表id
     * @param parameterMap 请求参数
     * @param is           请求流
     * @return 支付是否成功
     * @throws IOException IOException
     *                     拦截器相关增加， 详情查看{@link PayService#addPayMessageInterceptor(PayMessageInterceptor)}
     *                     <p>
     *                     业务处理在对应的PayMessageHandler里面处理，在哪里设置PayMessageHandler，详情查看{@link PayService#setPayMessageHandler(PayMessageHandler)}
     *                     </p>
     *                     如果未设置 {@link PayMessageHandler} 那么会使用默认的 {@link DefaultPayMessageHandler}
     */
    @Override
    public String payBack(String detailsId, Map<String, String[]> parameterMap, InputStream is) throws IOException {
        //业务处理在对应的PayMessageHandler里面处理，在哪里设置PayMessageHandler，详情查看PayService.setPayMessageHandler()
        PaymentPlatformMerchantDetails details = detailsService.loadMerchantByMerchantId(detailsId);
        PayService payService = details.getPayService();
        return payService.payBack(parameterMap, is).toMessage();
    }


    /**
     * 查询
     *
     * @param order 订单的请求体
     * @return 返回查询回来的结果集，支付方原值返回
     */
    @Override
    public Map<String, Object> query(MerchantQueryOrder order) {
        PaymentPlatformMerchantDetails details = detailsService.loadMerchantByMerchantId(order.getDetailsId());
        return details.getPayService().query(order.getTradeNo(), order.getOutTradeNo());
    }

    /**
     * 交易关闭接口
     *
     * @param order 订单的请求体
     * @return 返回支付方交易关闭后的结果
     */
    @Override
    public Map<String, Object> close(MerchantQueryOrder order) {
        PaymentPlatformMerchantDetails details = detailsService.loadMerchantByMerchantId(order.getDetailsId());
        return details.getPayService().close(order.getTradeNo(), order.getOutTradeNo());
    }

    /**
     * 申请退款接口
     *
     * @param detailsId 列表id
     * @param order     订单的请求体
     * @return 返回支付方申请退款后的结果
     */
    @Override
    public RefundResult refund(String detailsId, RefundOrder order) {
        PaymentPlatformMerchantDetails details = detailsService.loadMerchantByMerchantId(detailsId);
        return details.getPayService().refund(order);
    }

    /**
     * 查询退款
     *
     * @param detailsId 列表id
     * @param order 订单的请求体
     * @return 返回支付方查询退款后的结果
     */
    @Override
    public Map<String, Object> refundQuery(String detailsId, RefundOrder order) {
        PaymentPlatformMerchantDetails details = detailsService.loadMerchantByMerchantId(detailsId);
        return details.getPayService().refundquery(order);
    }

    /**
     * 下载对账单
     *
     * @param order 订单的请求体
     * @return 返回支付方下载对账单的结果
     */
    @Override
    public Map<String, Object> downloadBill(MerchantQueryOrder order) {
        PaymentPlatformMerchantDetails details = detailsService.loadMerchantByMerchantId(order.getDetailsId());

        return details.getPayService().downloadbill(order.getBillDate(), order.getBillType());
    }

//    /**
//     * 通用查询接口，根据 TransactionType 类型进行实现,此接口不包括退款
//     *
//     * @param order 订单的请求体
//     * @return 返回支付方对应接口的结果
//     */
//    @Override
//    public Map<String, Object> secondaryInterface(MerchantQueryOrder order) {
//        PaymentPlatformMerchantDetails details = detailsService.loadMerchantByMerchantId(order.getDetailsId());
//        TransactionType type = details.getPaymentPlatform().getTransactionType(order.getWayTrade());
//        return details.getPayService().secondaryInterface(order.getTradeNoOrBillDate(), order.getOutTradeNoBillType(), type);
//    }


    /**
     * 转账
     *
     * @param detailsId 列表id
     * @param order     转账订单
     * @return 对应的转账结果
     */
    @Override
    public Map<String, Object> transfer(String detailsId, TransferOrder order) {
        PaymentPlatformMerchantDetails details = detailsService.loadMerchantByMerchantId(detailsId);
        return details.getPayService().transfer(order);
    }

    /**
     * 转账查询
     *
     * @param detailsId 列表id
     * @param outNo     商户转账订单号
     * @param tradeNo   支付平台转账订单号
     * @return 对应的转账订单
     */
    @Override
    public Map<String, Object> transferQuery(String detailsId, String outNo, String tradeNo) {
        PaymentPlatformMerchantDetails details = detailsService.loadMerchantByMerchantId(detailsId);
        return details.getPayService().transferQuery(outNo, tradeNo);
    }

    /**
     * 创建消息
     * @param detailsId 列表id
     * @param message 支付平台返回的消息
     * @return 支付消息对象
     */
    @Override
    public PayMessage createMessage(String detailsId, Map<String, Object> message){
        PaymentPlatformMerchantDetails details = detailsService.loadMerchantByMerchantId(detailsId);
        return details.getPayService().createMessage(message);
    }
}
