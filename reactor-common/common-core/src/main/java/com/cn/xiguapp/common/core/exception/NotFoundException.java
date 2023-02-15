package com.cn.xiguapp.common.core.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author xiguaapp
 * @desc
 * @since 1.0 15:39
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundException extends ApiException {
    public NotFoundException(String errCode, String errMsg, String subErrCode, String subErrMsg){
        super(errCode, errMsg, subErrCode, subErrMsg);
    }
    public NotFoundException(String message){
        super("404",message,"20000","isp.unknown-error");
    }
    public NotFoundException(){
        super("记录不存在");
    }
}
