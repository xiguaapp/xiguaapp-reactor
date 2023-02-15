package com.cn.xiguaapp.common.gateway.result;

/**
 * @author xiguaapp
 * @Date 2020/10/12
 * @desc 对返回结果进行处理
 *  * 成功示例
 *  * {
 *  * "alipay_trade_fastpay_refund_query_response": {
 *  * "code": "10000",
 *  * "msg": "Success",
 *  * "trade_no": "2014112611001004680073956707",
 *  * "out_trade_no": "20150320010101001",
 *  * "out_request_no": "20150320010101001",
 *  * "refund_reason": "用户退款请求",
 *  * "total_amount": 100.2,
 *  * "refund_amount": 12.33
 *  * },
 *  * "sign": "ERITJKEIJKJHKKKKKKKHJEREEEEEEEEEEE"
 *  * }
 *  * <p>
 *  * 异常示例
 *  * {
 *  * "alipay_trade_fastpay_refund_query_response": {
 *  * "code": "20000",
 *  * "msg": "Service Currently Unavailable",
 *  * "sub_code": "isp.unknown-error",
 *  * "sub_msg": "系统繁忙"
 *  * },
 *  * "sign": "ERITJKEIJKJHKKKKKKKHJEREEEEEEEEEEE"
 *  * }
 */
public interface ResultExecutor<T,R>{
    /**
     * 合并结果
     * @param request
     * @param serviceResult
     * @return 返回最终输出结果
     */
    String mergeResult(T request, String serviceResult);

    /**
     * 合并错误结果
     * @param request
     * @param ex
     * @return 返回最终输出结果
     */
    R buildErrorResult(T request, Throwable ex);
}
