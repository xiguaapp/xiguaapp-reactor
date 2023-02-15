package com.cn.xiguaapp.common.gateway.result;

/**
 * @author xiguaapp
 * @Date 2020/10/12
 * @desc 固定返回值
 *  * {
 *  *     "result": {
 *  *         "code": "20000",
 *  *         "msg": "Service Currently Unavailable",
 *  *         "sub_code": "isp.unknown-error",
 *  *         "sub_msg": "系统繁忙"
 *  *     },
 *  *     "sign": "ERITJKEIJKJHKKKKKKKHJEREEEEEEEEEEE"
 *  * }
 */
public class CustomDataNameBuilder implements DataNameBuilder {
    private String dataName = "result";

    public CustomDataNameBuilder() {
    }

    public CustomDataNameBuilder(String dataName) {
        this.dataName = dataName;
    }

    @Override
    public String build(String method) {
        return dataName;
    }
}