package com.cn.ykyoung.server.exception;

import com.cn.ykyoung.server.message.ServiceError;
import com.cn.ykyoung.server.message.ServiceErrorEnum;
import com.cn.ykyoung.server.message.ServiceErrorImpl;

/**
 * @author xiguaapp
 * @project-name xiguaapp-reactor
 * @Date 2020/10/22
 * @desc
 */
public class ServiceException extends RuntimeException {
    private final transient ServiceError error;

    public ServiceException(String subCode, String subMsg) {
        super(subMsg);
        this.error = new ServiceErrorImpl(subCode, subMsg);
    }

    public ServiceException(String subMsg) {
        super(subMsg);
        String subCode = ServiceErrorEnum.ISV_COMMON_ERROR.getErrorMeta().getError().getSub_code();
        this.error = new ServiceErrorImpl(subCode, subMsg);
    }

    public ServiceException(ServiceError error) {
        super(error.toString());
        this.error = error;
    }

    public ServiceError getError() {
        return error;
    }
}
