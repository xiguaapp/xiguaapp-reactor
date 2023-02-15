package com.cn.xiguaapp.pay.zdemo.handler;

import com.cn.xiguaapp.xiguaapp.java.common.api.PayService;
import com.cn.xiguaapp.xiguaapp.java.common.core.PayOutMessage;
import com.cn.xiguaapp.xiguaapp.java.common.core.base.PayMessage;
import com.cn.xiguaapp.xiguaapp.java.common.exception.PayErrorException;
import com.cn.xiguaapp.pay.payoneer.api.PayoneerPayService;

import java.util.Map;

/**
 * @author xiguaapp
 * @desc
 * @since 1.0 22:44
 */
public class PayoneerMessageHandler extends BasePayMessageHandler {


    public PayoneerMessageHandler(Integer payId) {
        super(payId);
    }

    @Override
    public PayOutMessage handle(PayMessage payMessage, Map context, PayService payService) throws PayErrorException {
        //交易状态
        if ("0".equals(payMessage.getPayMessage().get(PayoneerPayService.CODE))) {
            /////这里进行成功的处理

            return payService.successPayOutMessage(payMessage);
        }

        return payService.getPayOutMessage("fail", "失败");
    }
}
