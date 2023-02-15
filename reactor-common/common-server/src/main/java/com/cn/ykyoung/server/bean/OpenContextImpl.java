package com.cn.ykyoung.server.bean;

import java.util.Map;

/**
 * @author xiguaapp
 */
public class OpenContextImpl implements OpenContext {
    private final Map<String, Object> requestParams;
    private Object bizObject;

    public OpenContextImpl(Map<String, Object> requestParams) {
        this.requestParams = requestParams;
    }

    public void setBizObject(Object bizObject) {
        this.bizObject = bizObject;
    }

    @Override
    public Map<String, Object> getParameterMap() {
        return requestParams;
    }

    @Override
    public Object getBizObject() {
        return bizObject;
    }

    @Override
    public String toString() {
        return requestParams.toString();
    }
}
