package com.cn.xiguaapp.common.oauth.core.servlet;

import com.cn.xiguaapp.common.oauth.core.exception.SaTokenException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author xiguaapp
 * @desc
 * @since
 */
public class SaTokenServletDefaultImpl implements SaTokenServlet {

    /**
     * 获取当前请求的Request对象
     */
    @Override
    public HttpServletRequest getRequest() {
        throw new SaTokenException("请实现SaTokenServlet接口后进行Servlet相关操作");
    }

    /**
     * 获取当前请求的Response对象
     */
    @Override
    public HttpServletResponse getResponse() {
        throw new SaTokenException("请实现SaTokenServlet接口后进行Servlet相关操作");
    }

}
