package com.cn.xiguaapp.pay.zdemo.handler;

import com.cn.xiguaapp.xiguaapp.java.common.core.PayOutMessage;
import com.cn.xiguaapp.xiguaapp.java.common.exception.PayErrorException;
import com.cn.xiguaapp.pay.unino.api.UnionPayService;
import com.cn.xiguaapp.pay.unino.constants.SDKConstants;
import com.cn.xiguaapp.pay.unino.core.UnionPayMessage;

import java.util.Map;

/**
 * @author xiguaapp
 * @desc
 * @since 1.0 22:45
 */
public class UnionPayMessageHandler extends BasePayMessageHandler<UnionPayMessage, UnionPayService> {


    public UnionPayMessageHandler(Integer payId) {
        super(payId);
    }

    @Override
    public PayOutMessage handle(UnionPayMessage payMessage, Map<String, Object> context, UnionPayService payService) throws PayErrorException {
        //交易状态
        if (SDKConstants.OK_RESP_CODE.equals(payMessage.getPayMessage().get(SDKConstants.param_respCode))) {
            /////这里进行成功的处理

            return payService.successPayOutMessage(payMessage);
        }

        return payService.getPayOutMessage("fail", "失败");
    }
}