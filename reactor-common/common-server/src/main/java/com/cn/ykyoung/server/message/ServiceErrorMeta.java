package com.cn.ykyoung.server.message;

import com.cn.ykyoung.server.bean.ServiceContext;
import com.cn.ykyoung.server.exception.ServiceException;
import lombok.Getter;

/**
 * @author xiguaapp
 * @project-name xiguaapp-reactor
 * @Date 2020/10/22
 * @desc 错误对象
 */
@Getter
public class ServiceErrorMeta {
    private String modulePrefix;
    private String subCode;
    public ServiceErrorMeta(String modulePrefix, String subCode) {
        this.modulePrefix = modulePrefix;
        this.subCode = subCode;
    }

    public ServiceError getError() {
        return ServiceErrorFactory
                .getError(this, ServiceContext.getCurrentContext()
                        .getLocale());
    }

    /**
     * 返回网关exception
     *
     * @param params 参数
     * @return 返回exception
     */
    public ServiceException getException(Object... params) {
        ServiceError error = ServiceErrorFactory.getError(this, ServiceContext.getCurrentContext().getLocale(), params);
        return new ServiceException(error);
    }
}
