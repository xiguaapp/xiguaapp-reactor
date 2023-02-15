package com.cn.xiguaapp.pay.zdemo.controller;

import com.cn.xiguaapp.pay.fuiou.api.FuiouPayConfigStorage;
import com.cn.xiguaapp.pay.fuiou.api.FuiouPayService;
import com.cn.xiguaapp.pay.fuiou.constants.FuiouTransactionType;
import com.cn.xiguaapp.xiguaapp.java.common.api.PayMessageHandler;
import com.cn.xiguaapp.xiguaapp.java.common.api.PayService;
import com.cn.xiguaapp.xiguaapp.java.common.api.impl.DefaultPayMessageHandler;
import com.cn.xiguaapp.xiguaapp.java.common.core.base.PayOrder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

/**
 * @author xiguaapp
 * @desc 发起支付入口
 * @since 1.0 22:55
 */
@RestController
@RequestMapping("fuiou")
public class FuiouPayController {

    private PayService service = null;


    @PostConstruct
    public void init() {
        FuiouPayConfigStorage fuiouPayConfigStorage = new FuiouPayConfigStorage();
        fuiouPayConfigStorage.setMchntCd("合作者id");
        fuiouPayConfigStorage.setKeyPublic("支付密钥");
        fuiouPayConfigStorage.setKeyPrivate("支付密钥");
        fuiouPayConfigStorage.setNotifyUrl("异步回调地址");
        fuiouPayConfigStorage.setReturnUrl("同步回调地址");
        fuiouPayConfigStorage.setSignType("MD5");
        fuiouPayConfigStorage.setInputCharset("utf-8");
        //是否为测试账号，沙箱环境
        fuiouPayConfigStorage.setTest(true);


        service = new FuiouPayService(fuiouPayConfigStorage);


        //设置回调消息处理
        //TODO {@link com.egzosn.pay.demo.controller.FuiouPayController#payBack}
//        service.setPayMessageHandler(new FuiouPayMessageHandler(null));
    }



    /**
     * 跳到支付页面
     * 针对实时支付
     *
     * @param price       金额
     * @return 跳到支付页面
     */
    @RequestMapping(value = "toPay.html", produces = "text/html;charset=UTF-8")
    public String toPay( BigDecimal price) {
        //支付订单基础信息
        PayOrder order = new PayOrder("订单title", "摘要",  BigDecimal.valueOf(0.01) , UUID.randomUUID().toString().replace("-", "").substring(2));
        order.setTransactionType(FuiouTransactionType.B2C);
        //获取支付所需的信息
//        Map<String, Object> directOrderInfo = service.orderInfo(order);
        //获取表单提交对应的字符串，将其序列化到页面即可,
//       return service.buildRequest(directOrderInfo, MethodType.POST);
        return service.toPay(order);
    }


    /**
     * 支付回调地址 方式一
     *
     * 方式二，{@link #payBack(HttpServletRequest)} 是属于简化方式， 试用与简单的业务场景
     *
     * @param request 请求
     *
     * @return 是否成功
     * @throws IOException IOException
     * @see #payBack(HttpServletRequest)
     */
    @Deprecated
    @RequestMapping(value = "payBackBefore.json")
    public String payBackBefore(HttpServletRequest request) throws IOException {

        //获取支付方返回的对应参数
        Map<String, Object> params = service.getParameter2Map(request.getParameterMap(), request.getInputStream());
        if (null == params) {
            return service.getPayOutMessage("fail", "失败").toMessage();
        }

        //校验
        if (service.verify(params)) {
            //这里处理业务逻辑
            //......业务逻辑处理块........
            return service.successPayOutMessage(null).toMessage();
        }

        return service.getPayOutMessage("fail", "失败").toMessage();
    }
    /**
     * 支付回调地址
     *
     * @param request 请求
     *
     * @return 是否成功
     *
     * 业务处理在对应的PayMessageHandler里面处理，在哪里设置PayMessageHandler，详情查看{@link PayService#setPayMessageHandler(com.cn.xiguaapp.pay.java.common.utils.encrypt.PayMessageHandler)}
     *
     * 如果未设置 {@link PayMessageHandler} 那么会使用默认的 {@link DefaultPayMessageHandler}
     * @throws IOException IOException
     */
    @RequestMapping(value = "payBack.json")
    public String payBack(HttpServletRequest request) throws IOException {
        //业务处理在对应的PayMessageHandler里面处理，在哪里设置PayMessageHandler，详情查看com.cn.xiguaapp.pay.java.common.utils.encrypt.PayService.setPayMessageHandler()
        return service.payBack(request.getParameterMap(), request.getInputStream()).toMessage();
    }

}
