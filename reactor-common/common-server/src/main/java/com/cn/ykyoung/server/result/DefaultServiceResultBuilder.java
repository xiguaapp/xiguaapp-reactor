package com.cn.ykyoung.server.result;

import com.cn.xiguapp.common.core.exception.ServiceResultBuilder;
import com.cn.ykyoung.server.exception.ServiceException;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

/**
 * @author xiguaapp
 * @project-name xiguaapp-reactor
 * @Date 2020/10/22
 * @desc 自定义错误返回结果实现
 */
public class DefaultServiceResultBuilder implements ServiceResultBuilder {
    public static final String ISP_UNKNOWN_ERROR = "isp.unknown-error";;
    @Override
    public Object buildError(String subCode, String subMsg) {
        return new AlipayResult().setSub_code(subCode).setSub_msg(subMsg);
    }

    @Override
    public Object buildError(ServerRequest request, ServerResponse response, Throwable throwable) {
        String sub_code,sub_msg;
        if (throwable instanceof ServiceException){
            ServiceException ex = (ServiceException) throwable;
            sub_code = ex.getError().getSub_code();
            sub_msg = ex.getError().getSub_msg();
        }else {
            sub_code = ISP_UNKNOWN_ERROR;
            sub_msg = throwable.getMessage();
        }
        return this.buildError(sub_code, sub_msg);
    }
}
