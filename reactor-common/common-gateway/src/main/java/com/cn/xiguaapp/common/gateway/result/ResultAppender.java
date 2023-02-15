package com.cn.xiguaapp.common.gateway.result;

import com.alibaba.fastjson.JSONObject;

import java.util.Map;

/**
 * @author xiguaapp
 * @Date 2020/10/12
 * @desc 对结果进行追加内容
 */
public interface ResultAppender {
    /**
     * 追加最终结果
     *
     * @param result 最终结果
     * @param params 请求参数
     * @param ctx    请求上下文，zuul对应的是RequestContext，Gateway对应的是exchange
     */
    void append(JSONObject result, Map<String, Object> params, Object ctx);

}
