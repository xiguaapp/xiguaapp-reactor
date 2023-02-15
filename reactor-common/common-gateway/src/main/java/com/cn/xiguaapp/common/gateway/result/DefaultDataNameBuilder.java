package com.cn.xiguaapp.common.gateway.result;

/**
 * @author xiguaapp
 * @Date 2020/10/12
 * @desc 将方法名中的"."转成"_"并在后面追加"_response"<br>
 *  * 如：alipay.trade.order.settle --> alipay_trade_order_settle_response<br>
 *  * <pre>
 *  * {
 *  *     "alipay_trade_order_settle_response": {
 *  *         "code": "20000",
 *  *         "msg": "Service Currently Unavailable",
 *  *         "sub_code": "isp.unknown-error",
 *  *         "sub_msg": "系统繁忙"
 *  *     },
 *  *     "sign": "ERITJKEIJKJHKKKKKKKHJEREEEEEEEEEEE"
 *  * }
 *  * </pre>
 */
public class DefaultDataNameBuilder implements DataNameBuilder {private static final char DOT = '.';
    private static final char UNDERLINE = '_';
    private static final String DATA_SUFFIX = "_response";

    @Override
    public String build(String method) {
        if (method == null) {
            method = "error";
        }
        return method.replace(DOT, UNDERLINE) + DATA_SUFFIX;
    }
}
