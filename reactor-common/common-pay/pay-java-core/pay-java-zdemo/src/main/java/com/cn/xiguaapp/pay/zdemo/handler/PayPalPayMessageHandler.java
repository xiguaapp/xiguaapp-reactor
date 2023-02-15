package com.cn.xiguaapp.pay.zdemo.handler;

import com.cn.xiguaapp.xiguaapp.java.common.api.PayMessageHandler;
import com.cn.xiguaapp.xiguaapp.java.common.core.PayOutMessage;
import com.cn.xiguaapp.xiguaapp.java.common.core.base.PayMessage;
import com.cn.xiguaapp.xiguaapp.java.common.exception.PayErrorException;
import com.cn.xiguaapp.pay.paypal.api.PayPalPayService;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author xiguaapp
 * @desc
 * @since 1.0 22:44
 */
@Component
public class PayPalPayMessageHandler implements PayMessageHandler<PayMessage, PayPalPayService> {

    @Override
    public PayOutMessage handle(PayMessage payMessage, Map<String, Object> context, PayPalPayService payService) throws PayErrorException {


        return payService.getPayOutMessage("fail", "失败");
    }
}
