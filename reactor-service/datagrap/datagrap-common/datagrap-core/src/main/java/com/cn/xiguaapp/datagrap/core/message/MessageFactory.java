package com.cn.xiguaapp.datagrap.core.message;


import com.cn.xiguaapp.datagrap.core.message.impl.MessageListImpl;
import com.cn.xiguaapp.datagrap.core.message.impl.MessageObjectImpl;

/**
 * @author xiguaapp
 * @date 2021-02-21
 * @desc 创建消息题
 */
public class MessageFactory {
    /**
     * 创建一个MessageObject
     * @return
     */
    public static MessageObject getMessageObject(){
        return new MessageObjectImpl();
    }

    /**
     * 创建一个MessageList
     * @return
     */
    public static MessageList getMessageList(){
        return new MessageListImpl();
    }

}
