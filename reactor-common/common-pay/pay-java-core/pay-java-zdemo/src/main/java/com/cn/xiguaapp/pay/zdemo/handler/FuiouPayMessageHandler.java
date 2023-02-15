package com.cn.xiguaapp.pay.zdemo.handler;

import com.cn.xiguaapp.xiguaapp.java.common.api.PayService;
import com.cn.xiguaapp.xiguaapp.java.common.core.PayOutMessage;
import com.cn.xiguaapp.xiguaapp.java.common.core.base.PayMessage;
import com.cn.xiguaapp.xiguaapp.java.common.exception.PayErrorException;

import java.util.Map;

/**
 * @author xiguaapp
 * @desc
 * @since 1.0 22:43
 */
public class FuiouPayMessageHandler extends BasePayMessageHandler {




    public FuiouPayMessageHandler(Integer payId) {
        super(payId);
    }

    @Override
    public PayOutMessage handle(PayMessage payMessage, Map context, PayService payService) throws PayErrorException {
        //交易状态
        if ("0000".equals(payMessage.getPayMessage().get("order_pay_code"))){
            /////这里进行成功的处理

            return PayOutMessage.JSON().content("success","成功").build();
        }

        return PayOutMessage.JSON().content("fail", "失败").build();
    }
}
