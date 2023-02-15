package com.cn.ykyoung.server.message;

/**
 * @author xiguaapp
 * @project-name xiguaapp-reactor
 * @Date 2020/10/22
 * @desc
 */
public enum ServiceErrorEnum {
    /** 系统繁忙 */
    ISP_UNKNOW_ERROR("isp.unknown-error"),
    /** 参数错误 */
    ISV_PARAM_ERROR("isv.invalid-parameter"),
    /** 通用错误 */
    ISV_COMMON_ERROR("isv.common-error"),
    ;
    private ServiceErrorMeta errorMeta;

    ServiceErrorEnum(String subCode) {
        this.errorMeta = new ServiceErrorMeta("isp.error_", subCode);
    }

    public ServiceErrorMeta getErrorMeta() {
        return errorMeta;
    }
}
