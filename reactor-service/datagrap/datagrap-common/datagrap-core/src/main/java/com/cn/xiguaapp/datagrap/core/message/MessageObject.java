package com.cn.xiguaapp.datagrap.core.message;

import java.util.Map;

/**
 * @author xiguaapp
 * @data 2021-02-21
 * @desc 消息
 */
public interface MessageObject extends Map<String,Object> {
    /**
     * 获取一个MessageObject
     * @param key
     * @return
     */
    MessageObject getObject(String key);

    /**
     * 获取一个MessageList
     * @param key
     * @return
     */
    MessageList getList(String key);


}
