package com.cn.xiguaapp.datagrap.core.message.impl;

import com.alibaba.fastjson.JSON;
import com.cn.xiguaapp.datagrap.core.message.MessageList;
import com.cn.xiguaapp.datagrap.core.message.MessageObject;

import java.util.Vector;

/**
 * @author xiguaapp
 * @date 2021-02-21
 * @desc 实现类
 */
public class MessageListImpl<V> extends Vector<V> implements MessageList<V> {

    @Override
    public MessageObject getObject(int index) {
        Object o = get(index);
        if (o instanceof MessageObject) {
            return (MessageObject) o;
        }
        throw new ClassCastException();
    }

    @Override
    public MessageList getList(int index) {
        Object o = get(index);
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
