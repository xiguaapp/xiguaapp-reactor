package com.cn.xiguaapp.xiguaapp.java.common.http;

import org.apache.http.Header;
import org.apache.http.message.BasicHeader;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author xiguaapp
 * @desc 专门用作支付类的请求头
 * @since 1.0 14:38
 */
public class HttpHeader {
    /**
     * 请求头
     */
    private List<Header> headers;

    public HttpHeader() {
    }

    public HttpHeader(List<Header> headers) {
        this.headers = headers;
    }

    /**
     * 请求头
     *
     * @param header 请求头
     */
    public HttpHeader(Header header) {
        addHeader(header);
    }

    /**
     * 获取请求头集
     *
     * @return 请求头集
     */
    public List<Header> getHeaders() {
        return headers;
    }

    /**
     * 添加请求头
     *
     * @param header 请求头
     */
    public void addHeader(Header header) {
        if (null == this.headers) {
            this.headers = new ArrayList<>();
        }
        this.headers.add(header);
    }

    /**
     * 设置请求头集
     *
     * @param headers 请求头集
     */
    public void setHeaders(List<Header> headers) {
        this.headers = headers;
    }

    /**
     * 设置请求头集
     *
     * @param headers 请求头集
     */
    public void setHeaders(Map<String, String> headers) {
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            addHeader(new BasicHeader(entry.getKey(), entry.getValue()));
        }
    }
}
