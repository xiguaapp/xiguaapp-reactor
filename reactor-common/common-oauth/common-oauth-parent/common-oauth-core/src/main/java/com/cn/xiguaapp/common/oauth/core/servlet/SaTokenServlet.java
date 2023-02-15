package com.cn.xiguaapp.common.oauth.core.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author xiguaapp
 * @desc Servlet相关操作接口
 * @since
 */
public interface SaTokenServlet {

    /**
     * 获取当前请求的 Request 对象
     *
     * @return 当前请求的Request对象
     */
    public HttpServletRequest getRequest();

    /**
     * 获取当前请求的 Response 对象
     *
     * @return 当前请求的response对象
     */
    public HttpServletResponse getResponse();

}
