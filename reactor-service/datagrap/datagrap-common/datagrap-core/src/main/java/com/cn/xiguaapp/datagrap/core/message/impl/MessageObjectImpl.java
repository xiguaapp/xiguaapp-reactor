package com.cn.xiguaapp.datagrap.core.message.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cn.xiguaapp.datagrap.core.message.MessageList;
import com.cn.xiguaapp.datagrap.core.message.MessageObject;
import lombok.extern.slf4j.Slf4j;

/**
 * @author author
 * @date 2021-02-21
 * @desc
 */
@Slf4j
public class MessageObjectImpl extends JSONObject implements MessageObject {


    @Override
    public MessageObject getObject(String key) {
        Object o = get(key);
        if (o instanceof MessageObject) {
            return (MessageObject) o;
        }

        throw new ClassCastException();
    }

    @Override
    public MessageList getList(String key) {
        Object o = get(key);
        if (o instanceof MessageList) {
            return (MessageList) o;
        }
        throw new ClassCastException();
    }

    @Override
    public String toString() {

        return JSON.toJSONString(this);
    }

}
