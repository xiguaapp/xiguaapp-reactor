package com.cn.xiguaapp.common.gateway.bean;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

/**
 * @author xiguaapp
 * @Date 2020/10/10
 * @desc 渠道实体
 */
@Data
public class ChannelMsg {
    /**
     * 渠道名称
     */
    private String operation;
    /**
     * 实体(数据)
     */
    private JSONObject data;

    /**
     * json转实体
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T toObject(Class<T> clazz) {
        return data.toJavaObject(clazz);
    }
}
