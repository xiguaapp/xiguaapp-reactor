package com.cn.xiguaapp.common.gateway.interceptor;

import com.cn.xiguaapp.common.gateway.bean.ApiParam;
import org.springframework.cloud.client.ServiceInstance;

/**
 * @author xiguaapp
 * @Date 2020/10/12
 * @desc
 */
public class DefaultRouteInterceptorContext implements RouteInterceptorContext {

    /** 请求参数 */
    private ApiParam apiParam;
    /** 错误信息 */
    private String serviceErrorMsg;
    /** 微服务返回状态 */
    private int responseStatus;
    /** 开始时间 */
    private long beginTimeMillis;
    /** 结束时间 */
    private long finishTimeMillis;
    /** 请求上下文 */
    private Object requestContext;
    /** 微服务返回结果 */
    private String serviceResult;
    /** 请求包大小 */
    private long requestDataSize;
    /** 返回内容大小 */
    private long responseDataSize;
    /** 负载均衡选中的微服务 */
    private ServiceInstance serviceInstance;

    @Override
    public ApiParam getApiParam() {
        return apiParam;
    }

    @Override
    public String getServiceResult() {
        return serviceResult;
    }

    @Override
    public int getResponseStatus() {
        return responseStatus;
    }

    @Override
    public long getBeginTimeMillis() {
        return beginTimeMillis;
    }

    @Override
    public long getFinishTimeMillis() {
        return finishTimeMillis;
    }

    @Override
    public Object getRequestContext() {
        return requestContext;
    }

    public void setApiParam(ApiParam apiParam) {
        this.apiParam = apiParam;
    }

    public void setServiceResult(String serviceResult) {
        this.serviceResult = serviceResult;
    }

    public void setResponseStatus(int responseStatus) {
        this.responseStatus = responseStatus;
    }

    public void setBeginTimeMillis(long beginTimeMillis) {
        this.beginTimeMillis = beginTimeMillis;
    }

    public void setFinishTimeMillis(long finishTimeMillis) {
        this.finishTimeMillis = finishTimeMillis;
    }

    public void setRequestContext(Object requestContext) {
        this.requestContext = requestContext;
    }

    @Override
    public String getServiceErrorMsg() {
        return serviceErrorMsg;
    }

    public void setServiceErrorMsg(String serviceErrorMsg) {
        this.serviceErrorMsg = serviceErrorMsg;
    }

    @Override
    public long getRequestDataSize() {
        return requestDataSize;
    }

    public void setRequestDataSize(long requestDataSize) {
        // spring cloud gateway get请求contentLength返回-1
        if (requestDataSize < 0) {
            requestDataSize = 0;
        }
        this.requestDataSize = requestDataSize;
    }

    @Override
    public long getResponseDataSize() {
        return responseDataSize;
    }

    public void setResponseDataSize(long responseDataSize) {
        this.responseDataSize = responseDataSize;
    }

    @Override
    public ServiceInstance getServiceInstance() {
        return serviceInstance;
    }

    public void setServiceInstance(ServiceInstance serviceInstance) {
        this.serviceInstance = serviceInstance;
    }
}
